package com.along101.contract.service;

import com.along101.contract.entity.ReleaseHistoryEntity;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.google.common.collect.Lists;
import com.along101.contract.common.exception.ContractException;
import com.along101.contract.common.utils.ConvertUtils;
import com.along101.contract.entity.FileHistoryEntity;
import com.along101.contract.entity.ReleaseHistoryEntity;
import com.along101.contract.vo.ProtoFileVO;
import com.along101.contract.vo.VersionInfo;
import com.along101.raptor.codegen.java.PomGenerator;
import com.along101.raptor.codegen.java.maven.PomModel;
import com.along101.raptor.codegen.swagger.RaptorSwaggerConverter;
import com.squareup.wire.schema.DBSchemaLoader;
import com.squareup.wire.schema.ProtoFile;
import com.squareup.wire.schema.Schema;
import com.squareup.wire.schema.Service;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author zhangchengxi
 * Date 2018/5/7
 */
@Component
public class CodegenService {

    public static final String ZIP_SEP = "/";
    @Autowired
    private DBSchemaLoader dbSchemaLoader;
    @Autowired
    private ProtoFileService protoFileService;
    @Autowired
    private VersionService versionService;
    @ApolloConfig
    private Config config;


    /**
     * 生成 appId下的swagger json
     *
     * @param outputStream
     * @param appId
     */
    public void generateSwaggerJson(OutputStream outputStream, String appId, String revisionNumber) {
        Schema schema = dbSchemaLoader.loadFromDBByAppIdAndVersion(appId, revisionNumber);
        RaptorSwaggerConverter raptorSwaggerConverter = new RaptorSwaggerConverter(schema);
        List<OpenAPI> openAPIS = raptorSwaggerConverter.convert();

        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

        try {
            for (OpenAPI openAPI : openAPIS) {
                zipOutputStream.putNextEntry(new ZipEntry(openAPI.getInfo().getTitle() + ".json"));
                zipOutputStream.write(Json.pretty(openAPI).getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            throw new RuntimeException("write swagger json to zip error", e);
        } finally {
            try {
                zipOutputStream.close();
            } catch (IOException e) {
                // TODO: 2018/5/8 L2 优化异常处理
            }
        }
    }


    /**
     * 获取fileId对应的swagger
     * 如果version 为draft 则 navigateTreeVO.protoId 为 proto_file 主键 ,否则为 file_history 主键
     */
    public List<OpenAPI> findSwaggerJsons(Long fileId, String revision, String appId) {
        ProtoFileVO protoFileVO;
        if (VersionInfo.isDraft(revision)) {
            protoFileVO = protoFileService.findContractById(fileId);
            ReleaseHistoryEntity latestReleaseHistory = versionService.getLatestReleaseHistory(appId);
            if (Objects.nonNull(latestReleaseHistory)) {
                revision = latestReleaseHistory.getRevisionNumber();
            }
        } else {
            FileHistoryEntity releasedProtoFile = versionService.getReleasedProtoFile(fileId);
            protoFileVO = ConvertUtils.convert(releasedProtoFile, ProtoFileVO.class);
        }
        if (Objects.isNull(protoFileVO)) {
            throw new ContractException("proto file not exist ,fileId=" + fileId);
        }

        Schema schema = dbSchemaLoader.loadFromDBByAppIdAndVersion(appId, revision);

        String protoName = protoFileVO.getName().substring(0, protoFileVO.getName().length() - ".proto".length());


        RaptorSwaggerConverter raptorSwaggerConverter = new RaptorSwaggerConverter(schema) {
            @Override
            public List<OpenAPI> convert() {
                List<OpenAPI> openAPIS = Lists.newArrayList();
                List<ProtoFile> protoFiles = schmea.protoFiles().stream()
                        .filter(protoFile -> protoFileVO.getPackageName().equals(protoFile.packageName())
                                && protoFile.name().equals(protoName))
                        .collect(Collectors.toList());

                for (ProtoFile protoFile : protoFiles) {
                    for (Service service : protoFile.services()) {
                        OpenAPI openAPI = getOpenApi(service, protoFile);
                        openAPIS.add(openAPI);
                    }
                }
                return openAPIS;
            }
        };

        List<OpenAPI> openAPIS = raptorSwaggerConverter.convert();
        return openAPIS;
    }


    // 生成server端项目
    public void generateServerProject(OutputStream outputStream, PomModel pomModel, String appId, String revisionNumber) {
        pomModel.setExamplePom(config.getProperty("contract.generator.pom",""));

        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

        try {
            Path basePath = Paths.get(pomModel.getArtifactId());
            Path protoPath = basePath.resolve("src/main/proto");
            appendProtoFile(zipOutputStream, protoPath, appId, revisionNumber);
            appendPomFile(zipOutputStream, pomModel);
        } catch (IOException e) {
            // TODO: 2018/5/8 L2 优化异常处理
            throw new RuntimeException("generate server project file error", e);
        } finally {
            try {
                zipOutputStream.close();
            } catch (IOException e) {
                // TODO: 2018/5/8 L2 优化异常处理
            }
        }
    }

    public void generateProtoFile(ServletOutputStream outputStream, PomModel pomModel, String appId, String revisionNumber) {
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

        Path protoPath = Paths.get("");

        try {
            appendProtoFile(zipOutputStream, protoPath, appId, revisionNumber);
        } catch (IOException e) {
            // TODO: 2018/5/8 L2 优化异常处理
            throw new RuntimeException("write proto file error", e);
        } finally {
            try {
                zipOutputStream.close();
            } catch (IOException e) {
                // TODO: 2018/5/8 L2 优化异常处理
            }
        }

    }


    private void appendPomFile(ZipOutputStream zipOutputStream, PomModel pomModel) throws IOException {
        String name = pomModel.getArtifactId() + ZIP_SEP + "pom.xml";
        ZipEntry zipEntry = new ZipEntry(name);
        zipOutputStream.putNextEntry(zipEntry);
        PomGenerator pomGenerator = new PomGenerator(pomModel);
        pomGenerator.writeTo(zipOutputStream);
    }

    private void appendProtoFile(ZipOutputStream zipOutputStream, Path protoPath, String appId, String revisionNumber) throws IOException {
        List<ProtoFileVO> contracts = versionService.queryReleasedProtoFileVOs(appId, revisionNumber);

        for (ProtoFileVO contract : contracts) {
            Path protoFile = protoPath.resolve(contract.getPackageName().replace(".", ZIP_SEP)).resolve(contract.getName());
            ZipEntry zipEntry = new ZipEntry(protoFile.toString());
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.write(contract.getContent().getBytes(StandardCharsets.UTF_8));
            zipOutputStream.flush();
        }
    }


}

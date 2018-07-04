package com.along101.contract.service;

import com.google.common.collect.ImmutableList;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.along101.contract.ContractBackendApplicationTests;
import com.along101.raptor.codegen.java.maven.PomModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author zhangchengxi
 * Date 2018/5/8
 */
public class CodegenServiceTest extends ContractBackendApplicationTests{

    @Autowired
    private CodegenService codegenService;

    @Test
    public void testJimfs() throws IOException {
        FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
        Path foo = fs.getPath("/foo");
        Files.createDirectory(foo);

        Path abc = foo.resolve("abc.text");
        Files.write(abc, ImmutableList.of("hello world"), StandardCharsets.UTF_8);

        Iterable<FileStore> fileStores = fs.getFileStores();
        for (FileStore fileStore : fileStores) {
            System.out.println(fileStore.name());
        }

        boolean regularFile = Files.isRegularFile(abc);
        System.out.println(regularFile);

    }

    @Test
    public void testenerateServerProject() throws IOException {
        FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
        Path dir = fs.getPath("/temp");
        Files.createDirectory(dir);
        Path codeZip = fs.getPath("/temp/code.zip");
        Files.createFile(codeZip);
        OutputStream outputStream = Files.newOutputStream(codeZip);


        PomModel build = PomModel.builder().build();
        codegenService.generateServerProject(outputStream,build,"111","");
    }



    @Test
    public void test() throws IOException {
        File f = new File("project.zip");
        if(f.exists()){
            f.delete();
        }
        boolean newFile = f.createNewFile();

        FileOutputStream fileOutputStream = new FileOutputStream(f);

        PomModel build = PomModel.builder().build();
        codegenService.generateServerProject(fileOutputStream,build,"111","");
    }




}

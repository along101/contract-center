<template>
    <div class="toolbar">
        <el-row>
            <el-col :span="24">
                <div style="margin: 0 auto; float: right">

                    <el-button size="small" style="" @click="downloadDialogVisible=true">
                        代码生成
                        <i class="el-icon-download el-icon--right"></i>
                    </el-button>
                    <el-dropdown split-button type="small"
                                 @click="gotoVersionListPage"
                                 @command="chooseTag">
                        版本: {{revisionName}}
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item command="draft" v-if="isAppAdmin">当前版本</el-dropdown-item>
                            <el-dropdown-item v-for="releaseHistory in releaseHistoryList"
                                              v-if="releaseHistory.tag"
                                              :key="releaseHistory.id"
                                              :command="releaseHistory.revisionNumber">
                                {{releaseHistory.tag}}
                            </el-dropdown-item>
                            <el-dropdown-item command="more" v-if="isAppAdmin">更多...</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>

                    <el-upload
                            action=""
                            :data="uploadData"
                            :onError="uploadError"
                            multiple
                            :onSuccess="uploadSuccess"
                            :beforeUpload="beforeAvatarUpload"
                            style="display: inline-block;margin-left: 10px;margin-right: 10px;">
                        <el-button size="small"
                                   v-if="isAppAdmin">
                            上传
                            <i class="el-icon-upload el-icon--right"></i>
                        </el-button>
                    </el-upload>

                    <el-button size="small" style="" v-if="this.isAppAdmin" @click="addContract">
                        新增
                        <i class="el-icon-plus el-icon--right"></i>
                    </el-button>

                    <el-button size="small" style="" @click="editCurrentProtoFile"
                               v-if="this.isAppAdmin && this.fileId">
                        编辑
                        <i class="el-icon-edit el-icon--right"></i>
                    </el-button>

                    <el-button type="danger" size="small" style="" v-if="this.isAppAdmin && this.fileId"
                               @click="openDeleteCheck">
                        删除
                        <i class="el-icon-delete el-icon--right"></i>
                    </el-button>


                    <slot></slot>
                    <el-dialog
                            class="parentElement"
                            title="提示"
                            :visible.sync="downloadDialogVisible"
                            width="400px">
                        <div class="parentElement">
                            <table style=" padding-left: 50px;">
                                <tr>
                                    <td>GroupId:</td>
                                    <td>
                                        <el-input v-model="pomModel.groupId"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>ArtifactId:</td>
                                    <td>
                                        <el-input v-model="pomModel.artifactId"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Version:</td>
                                    <td>
                                        <el-input v-model="pomModel.version"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>下载类型:</td>
                                    <td>
                                        <el-select v-model="pomModel.mode" placeholder="请选择">
                                            <el-option
                                                    v-for="item in options"
                                                    :key="item.value"
                                                    :label="item.label"
                                                    :value="item.value">
                                            </el-option>
                                        </el-select>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Version:</td>
                                    <td>
                                        <el-input :disabled="true" v-model="revisionName"/>
                                    </td>
                                </tr>

                            </table>

                        </div>
                        <span slot="footer" class="dialog-footer">
                            <el-button @click="downloadDialogVisible = false">取消</el-button>
                            <el-button type="primary" @click="download">确 定</el-button>
                        </span>
                    </el-dialog>
                </div>

            </el-col>
        </el-row>

        <el-dialog title="增加契约" :visible.sync="dialogVisible">

            <el-form label-position="right" :model="inEditContract" ref="inEditContractForm" :rules="rules">

                <el-form-item prop="name">
                    <el-input placeholder="请输入文件名字" v-model="inEditContract.name">
                        <template slot="append">.proto</template>
                    </el-input>
                </el-form-item>

                <el-form-item prop="name">
                    <el-input placeholder="请输入包名" v-model="inEditContract.packageName">

                    </el-input>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="addProto('inEditContractForm')">保存</el-button>
                    <el-button @click="onClose" class="close-button">关闭</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

    </div>

</template>
<script>
    import {mapGetters} from 'vuex';
    import {stringify} from 'querystring'
    import router from '../../router';

    export default {

        data() {
            return {
                options: [
                    {value: "client", label: "客户端"},
                    {value: "server", label: "服务端"},
                    {value: "file", label: "Proto 文件"},
                    {value: "swagger", label: "Swagger 文件"},
                ],
                pomModel: {
                    groupId: "com.along101",
                    artifactId: "raptor-demo",
                    version: "0.0.1-SNAPSHOT",
                    mode: "client",
                    revision: ""
                },

                dialogVisible: false,

                inEditContract: {
                    name: '',
                    appid: this.appId,
                    packageName: '',
                    content: '',
                    status: 0,
                    isActive: true
                },

                downloadDialogVisible: false,

                uploadData: {},

                rules: {}

            };

        },

        computed: {
            ...mapGetters({
                protoId: 'getCurrentFileId',
                appId: 'getAppId',
                userOrg: 'getUserOrg',
                userName: 'getUserName',
                appInfo: 'getProjectsByAppId',
                showType: 'getShowType',
                serverUrl: 'getServerUrl',
                revisionNumber: 'getRevisionNumber',
                releaseHistoryList: 'getReleaseHistoryList',
                isAppAdmin: 'isAppAdmin'
            }),
            fileId() {
                return this.$route.query.fileId;
            },
            revisionName(){
                return this.gerRevisionOrTag(this.revisionNumber) === 'draft'? '当前版本' : this.gerRevisionOrTag(this.revisionNumber)
            }
        },

        created() {
            this.$store.dispatch('fetchProjectByAppId', {"appId": this.appId});
            this.$store.dispatch('queryReleaseHistoryList', this.appId);
        },

        methods: {
            gerRevisionOrTag(revisionNumber) {
                for (let releaseHistory of this.releaseHistoryList) {
                    if (revisionNumber === releaseHistory.revisionNumber) {
                        return releaseHistory.tag || revisionNumber;
                    }
                }
                return revisionNumber;
            },
            chooseTag(revisionNumber) {
                // 判断是否是"more"
                if ("more" === revisionNumber) {
                    this.gotoVersionListPage();
                    return;
                }
                let query = this.$route.query;

                this.$router.replace({
                    path: this.$route.path, query: {
                        ...query,
                        revision: revisionNumber,
                    }
                });

                this.$store.dispatch('checkoutVersionNumber', {
                    appId: this.appId,
                    revisionNumber: revisionNumber
                })
            },
            gotoVersionListPage() {
                router.push({name: "VersionList", query: this.$route.query})
            },

            toggle() {
                if (this.showType === 'doc') {
                    router.push({name: "ProtoContent", query: this.$route.query})
                } else {
                    router.push({name: "Document", query: this.$route.query})
                }
                this.$store.commit('changeShowType');

            },
            openDeleteCheck() {

                this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    console.log("a");
                    this.deleteProtoFile();
                    console.log("c");

                    this.$message({
                        type: 'success',
                        message: '删除成功!'
                    });
                }).catch((r) => {
                    console.log("b");
                    console.log(r);

                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },

            checkProtoFile() {
                let checkInfo = {
                    id: this.$route.query.fileId,
                    appid: this.appId,
                };
                let data = {
                    appId: this.appId,
                    newContract: checkInfo
                };
                this.$store.dispatch('checkEditProtoContent', data);
            },

            //用于页面编辑当前 proto 文件
            editCurrentProtoFile() {
                this.$router.push({
                    path: '/editCurrentProtoFile',
                    query: {appId: this.appId, fileId: this.$route.query.fileId, editFlag: true}
                });
            },

            deleteProtoFile() {
                console.log("delete protoFile :" + this.$route.query.fileId + " appId=" + this.appId);
                if (this.$route.query.fileId) {
                    this.$store.dispatch('deleteProtoFileById', {fileId: this.$route.query.fileId, appId: this.appId});
                }
            },

            download() {
                this.pomModel.revision = this.revisionNumber;
                this.downloadDialogVisible = false;
                let url = this.serverUrl + "/web/contracts/zip/" + this.appId;
                let query = stringify(this.pomModel);
                url = url + "?" + query;
                window.location.href = url;
            },

            handleRemove(file, fileList) {
                console.log(file, fileList);
            },
            handlePreview(file) {
                console.log(file);
            },

            handleRemove(file, fileList) {
                console.log(file, fileList);
            },
            handlePreview(file) {
                console.log(file);
            },

            uploadError(file) {
            },

            uploadSuccess(file) {
            },

            //上传文件的动作
            beforeAvatarUpload(file) {

                //定义self, 否则获取不到父组件传 过来的 props 属性
                let self = this;
                //self.imgType;

                console.log("appId=" + self.appId);

                let formData = new FormData();
                formData.append('file', file);
                formData.append("appId", self.appId);
                formData.append("orgId", 1);
                //let newProtoFile = formData;

                this.$store.dispatch('uploadNewProtoFile', formData);

                return true;

            },

            addContract() {
                this.dialogVisible = true;
            },

            resetForm() {

            },

            //用于页面添加 proto
            addProto(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let self = this;
                        this.dialogVisible = false;
                        //重新赋值，否则not defied
                        self.inEditContract.appid = self.appId;

                        let content =
                            "syntax = \"proto3\";\n" +
                            "\n" +
                            "\n" +
                            "package " + self.inEditContract.packageName + ";\n" +
                            "\n" +
                            "\n" +
                            "option (appId) = \"" + self.appId + "\";\n";

                        self.inEditContract.name = self.inEditContract.name + ".proto";
                        self.inEditContract.content = content;

                        let data = {
                            appId: self.appId,
                            newContract: self.inEditContract,
                        };

                        this.$store.dispatch('addNewContract', data).then(res => {
                            //跳转到编辑页面
                            //debugger
                            let fileId = self.protoId;
                            console.log("页面添加proto 成功后的 fileId=" + fileId);
                            self.$router.push({
                                path: '/editCurrentProtoFile',
                                query: {
                                    appId: self.appId,
                                    fileId: fileId,
                                    //标记是 添加 proto, 不是 编辑当前 proto
                                    newFlag: 1
                                }
                            });
                        });

                    } else {
                        return false;
                    }
                });
            },

            onClose() {
                this.dialogVisible = false;
            }

        }

    }
</script>
<style scoped>
    .sidebar {
        display: block;
        position: absolute;
        width: 250px;
        left: 0;
        top: 70px;
        bottom: 0;
        background: #2E363F;
    }

    .el-dropdown__caret-button::before {
        background: rgb(220, 223, 230);
    }

    .sidebar > ul {
        height: 100%;
    }

    .sidebar i {
        margin-top: -3px;
    }

    .el-upload .el-upload--text {
        width: 100%;
        /*height: unset;*/
    }

    .parentElement {
        display: flex; /*Flex布局*/
        display: -webkit-flex; /* Safari */
        align-items: center; /*指定垂直居中*/
    }

    .el-dialog {
        width: 30%;
    }


</style>

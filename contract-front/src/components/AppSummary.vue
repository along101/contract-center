<template>
    <div>

        <div class="app-summary">
            <div style="max-width: 1600px;min-width: 800px">

                <el-row>
                    <h4 style="margin-top: 30px">契约列表</h4>
                </el-row>
                <el-row style="margin-top: 10px; padding-left: 10px;right: 10px">
                    <el-table :data="protoFiles"
                              empty-text="暂无数据"
                              ref="fileCols"
                              @selection-change="handleSelectionChange"
                              border fit>
                        <el-table-column v-if="isAppAdmin" type="selection" width="55" ></el-table-column>
                        <el-table-column label="文件名字" prop="name" align="center" sortable>
                            <template slot-scope="scope">
                                <span style="margin-left: 10px">{{ scope.row.name }}</span>
                                <span v-if="scope.row.status != 1" :class="['mytag',getStatusClass(scope.row.status)]">{{getStatusTag(scope.row.status)}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="概要说明" prop="summary" align="center"></el-table-column>
                        <el-table-column label="最后更新时间" prop="updateTime" align="center"></el-table-column>
                        <el-table-column label="更新者" prop="updateBy" align="center"></el-table-column>

                        <el-table-column label="发布状态" align="center">
                            <template slot-scope="props">
                                <span>{{getStatusString(props.row.status)}}</span>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-row>
            </div>
        </div>
        <el-row class="tool-bar">
            <v-tool-bar :appId="this.appId">
                <el-button size="small" style="" @click="openReleaseDialog" v-if="isAppAdmin">
                    发布<i class="el-icon-check el-icon--right"></i></el-button>

                <el-button size="small" style="" type="danger" @click="openDeleteCheck" v-if="isAppAdmin">
                    删除<i class="el-icon-delete el-icon--right"></i></el-button>
            </v-tool-bar>
            <el-dialog
                    title="提示"
                    :visible.sync="releaseDialog"
                    width="400px">
                <div class="parentElement">
                    <table style=" padding-left: 50px;">
                        <tr>
                            <td>
                                tag :
                            </td>
                            <td>
                                <el-input v-model="tag" placeholder="tag" ></el-input>
                            </td>
                        </tr>
                    </table>
                </div>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="releaseDialog = false">取 消</el-button>
                    <el-button type="primary" @click="release">确 定</el-button>
                </span>
            </el-dialog>

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


        <el-dialog title="提示" :visible.sync="selectDialogVisible" width="30%">
            <span>未选择任何一个 proto 文件</span>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="selectDialogVisible = false">确定</el-button>
            </span>
        </el-dialog>

        <el-dialog title="提示" :visible.sync="allDeployedDialogVisible" width="30%">
            <span>所选中的所有 proto 文件均已发布，无需重新发布</span>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="allDeployedDialogVisible = false">确定</el-button>
            </span>
        </el-dialog>

    </div>
</template>

<script>
    import {mapGetters} from 'vuex';
    import vToolBar from './layout/ToolBar.vue'
    import {api} from '../api'
    import ElButton from "../../node_modules/element-ui/packages/button/src/button.vue";

    export default {
        components: {
            vToolBar},
        methods: {
            getStatusString(status){
              switch(status){
                  case 0:
                      return "新增";
                  case 1:
                      return "已发布";
                  case 2:
                      return "已修改";
                  case 3:
                      return "已删除";
              }
            },
            getStatusTag(status){
                switch(status){
                    case 0:
                        return "新";
                    case 1:
                        return "发";
                    case 2:
                        return "改";
                    case 3:
                        return "删";
                }
            },
            getStatusClass(status){
                switch(status){
                    case 0:
                        return "create";
                    case 1:
                        return "";
                    case 2:
                        return "update";
                    case 3:
                        return "delete";
                }
            },

            addContract() {
                this.dialogVisible = true;
            },

            openReleaseDialog() {
                //选择某 proto 文件
                if(this.multipleSelection.length == 0) {
                    this.selectDialogVisible = true;
                    return;
                }
                //todo, 已发布的无需发布
                //debugger

                let allDeployed = true;
                let unDeployedArray = [];
                for (var file of this.multipleSelection) {
                    let status = file.status;
                    if(status == 0) {
                        unDeployedArray.push(file);
                    }
                }

                if(unDeployedArray.length == 0) {
                    //alert("选中文件均已发布，无需重新发布");
                    this.allDeployedDialogVisible = true;
                    return;
                }else {
                    //只发布未发布的 proto
                    //debugger
                    this.multipleSelection = unDeployedArray;
                }

                this.releaseDialog = true;
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
                            this.$store.commit('updateFileChange');
                            let fileId = self.protoId;
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

            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            release() {
                let ids = this.multipleSelection.map(a => a.id);
                //debugger
                api.versionService.release(this.appId, this.tag, ids).then((res) => {
                    if(res.data.code !== -1){
                        this.$store.commit('updateFileChange');
                        // 更新 版本
                        this.$store.dispatch('queryReleaseHistoryList',this.appId);
                        //切换版本到当前版本
                        this.$store.dispatch('checkoutVersionNumber', {
                            appId: this.appId,
                            revisionNumber: 'draft',
                        });
                    }
                    this.$store.dispatch("displayPromptByResponseMsg", res);
                });
                this.releaseDialog = false;

            },

            onSearch() {
                console.log(this.pageSize + this.currentPage + this.currentField + this.currentValue);
                this.currentPage = 1;
                this.$store.dispatch('queryOrgs', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                });
            },

            openDeleteCheck() {

                if(this.multipleSelection.length == 0) {
                    this.selectDialogVisible = true;
                    return;
                }

                this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    console.log("a");
                    this.deleteProtoFiles();
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

            //用于删除多个 proto file
            deleteProtoFiles() {
                let fileIds = this.multipleSelection.map(a => a.id);
                console.log("delete protoFile :" + this.$route.query.fileId + " appId=" + this.appId);
                let data = {
                    ids: fileIds,
                    appId: this.appId
                };
                this.$store.dispatch('deleteProtoFileByMultiIds', data);

            },

            onClose() {
                this.dialogVisible = false;
            }

        },

        data: function () {
            return {
                fileId: null,
                multipleSelection: [],
                releaseDialog: false,
                tag: "",

                queryItem: {
                    modelName: '',
                    createdBy: ''
                },

                inEditContract: {
                    name: '',
                    appid: this.appId,
                    packageName: '',
                    content: '',
                    status: 0,
                    isActive: true
                },

                dialogVisible: false,
                selectDialogVisible: false,
                allDeployedDialogVisible: false,
                rules: {}

            };
        },

        computed: {
            ...mapGetters({
                appId: 'getAppId',
                filteredDescription: 'getFilteredDescription',
                currentApp: 'getProjectsByAppId',
                protoFiles: 'getProtoFileListByAppId',
                userName: 'getUserName',
                appInfo: 'getProjectsByAppId',
                isAppAdmin:'isAppAdmin',
                revisionNumber:'getRevisionNumber',
                fileChange:'getFileChange',
            }),
        },
        watch: {
            revisionNumber:function (revisionNumber) {
                this.$store.dispatch('queryProtoFileListByAppId', {appId:this.appId,revisionNumber:revisionNumber});
            },
            fileChange:function (){
                this.$store.dispatch('queryProtoFileListByAppId', {appId:this.appId,revisionNumber:this.revisionNumber});
            }
        },

        created() {
            console.log("appId appSummary");
        }

    };

</script>

<style>

    .mytag {
        display: inline;
        padding: .2em .6em .3em;
        font-size: 75%;
        font-weight: bold;
        line-height: 1;
        color: #fff;
        text-align: center;
        white-space: nowrap;
        vertical-align: baseline;
        border-radius: .25em;
    }

    .delete {
        background-color: #d9534f;
    }

    .update {
        background-color: #5bc0de;;
    }

    .create {
        background-color: #5cb85c;;
    }
    .app-summary {
        margin: 20px auto;
        position: absolute;
        top: -40px;
        bottom: 0;
        left: 20px;
        right: 0;
        overflow: auto;
    }

    .query-form {
        margin-top: 20px;
        padding: 25px;
        background-color: #eef1f6;
    }

    .close-button {
        float: right;
    }

    .add_button {
        float: right;
    }

    .text {
        font-size: 14px;
    }

    .item {
        padding: 18px 0;
    }

    .app-box-card {
        width: 100%;
        height: 130px;
        float: left;
        /*margin: 5px*/
    }

    .h5-card {
        margin-top: 40px;
        color: #909399;
    }

    .first-box-card {
        width: 1206px;
        height: 130px;
        /*margin: 5px*/
    }

    .app-info-title {
        color: #60616E;
    }
    .parentElement {
        display: flex; /*Flex布局*/
        display: -webkit-flex; /* Safari */
        align-items: center; /*指定垂直居中*/
    }



</style>


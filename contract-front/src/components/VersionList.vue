<template>
    <div>
        <v-tool-bar></v-tool-bar>

        <div class="releaseList">

            <el-row type="flex" class="query-form">
                <el-col :span="6">
                    <el-input v-model="keyword" placeholder="请输入关键字" style="width: 200px"></el-input>
                </el-col>
                <el-col :span="10">
                    <el-date-picker
                            v-model="dataRange"
                            type="daterange"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                            value-format="yyyy-MM-dd">
                    </el-date-picker>
                </el-col>
                <el-col :span="4" style="margin-top: 10px">
                    <span>Tag Only  </span>
                    <el-switch
                            v-model="tagOnly"
                    ></el-switch>
                </el-col>
                <el-col :span="4">
                    <el-button @click="clearCondition">重置</el-button>
                </el-col>


            </el-row>
            <br>


            <el-table :data="filteredReleaseHistoryList"
                      empty-text="暂无数据"
                      border fit
            >
                <el-table-column min-width="120" label="发布者" prop="insertBy" align="center" sortable></el-table-column>
                <el-table-column min-width="170" label="发布时间" prop="insertTime" align="center"
                                 sortable></el-table-column>
                <el-table-column min-width="345" label="版本号" prop="revisionNumber" align="center"></el-table-column>
                <el-table-column min-width="115" label="TAG" prop="tag" align="center"></el-table-column>
                <el-table-column min-width="160" label="操作" align="center">
                    <template slot-scope="props">
                        <el-button type="small" @click="checkout(props.row.revisionNumber)">查看</el-button>
                        <el-button type="small" v-if="isAppAdmin" @click="callRollbackDialog(props.row)">回退</el-button>
                        <el-button type="small" v-if="isAppAdmin" @click="callChangeTagDialog(props.row)">重打标签</el-button>
                    </template>
                </el-table-column>

            </el-table>
            <el-dialog
                    title="提示"
                    :visible.sync="showRollbackDialog"
                    width="30%">
                <div style="margin-left:30px">
                    回退操作无法撤销,请谨慎操作!!!
                </div>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="showRollbackDialog = false">取 消</el-button>
                    <el-button type="primary" @click="rollback()">确认回退</el-button>
                </span>
            </el-dialog>
            <el-dialog
                    title="提示"
                    :visible.sync="showChangeTagDialog"
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
                    <el-button @click="showChangeTagDialog = false">取 消</el-button>
                    <el-button type="primary" @click="changeTag">确 定</el-button>
                </span>
            </el-dialog>

        </div>


    </div>
</template>


<script>
    import {mapGetters} from 'vuex';
    import vToolBar from './layout/ToolBar.vue'
    import {stringify} from 'querystring'

    export default {
        data: function () {
            return {
                tagOnly: false,
                keyword: "",
                dataRange: [],
                selectRow: {},
                showRollbackDialog: false,
                showChangeTagDialog: false,
                tag:''
            }
        },
        components: {
            vToolBar
        },

        computed: {
            ...mapGetters({
                versionList: "getVersionList",
                appId: 'getAppId',
                releaseHistoryList: 'getReleaseHistoryList',
                appInfo: 'getProjectsByAppId',
                userName: 'getUserName',
                isAppAdmin: 'isAppAdmin',

            }),
            filteredReleaseHistoryList() {
                let that = this;
                return this.releaseHistoryList.filter((item) => {
                    return that.tagOnly ? item.tag : true;
                }).filter((item) => {
                    return that.keyword ? (item.tag + item.insertBy).indexOf(that.keyword) > -1 : true;
                }).filter((item) => {
                    return that.dataRange.length === 2 ? ((that.dataRange[0] + " 00:00:00") < item.insertTime && (that.dataRange[1] + " 23:59:59") > item.insertTime) : true;
                })
            }
        },

        created() {
            this.$store.dispatch('queryReleaseHistoryList', this.appId);
        },
        methods: {
            clearCondition() {
                this.tagOnly = false;
                this.keyword = '';
                this.dataRange = [];
            },
            checkout(revisionNumber) {
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
            rollback() {
                this.showRollbackDialog = false;
                this.$store.dispatch('rollback', this.selectRow )

            },
            callRollbackDialog(selectRow) {
                this.selectRow = selectRow;
                this.showRollbackDialog = true;
            },
            callChangeTagDialog(selectRow) {
                this.selectRow = selectRow;
                this.showChangeTagDialog = true;
            },
            changeTag(){
                this.showChangeTagDialog = false;
                this.$store.dispatch('changeTag', {id:this.selectRow.id,tag:this.tag,appId:this.appId} ).then((res) => {
                    this.$store.dispatch('queryReleaseHistoryList', this.appId);
                });

            }
        }


    }

</script>

<style>
    .releaseList {
        position: absolute;
        top: 40px;
        left: 0px;
        right: 0px;
        bottom: 0px;
        min-width: 900px;
        overflow: auto;
    }
</style>
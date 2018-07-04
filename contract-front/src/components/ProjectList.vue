<template>
    <div>
        <div>
            <el-row type="flex" class="query-form" justify="center">
            <!--    <el-col :span="4">
                </el-col>-->
                <el-col :span="4">

                    <el-select v-model="queryItem.appName"
                               placeholder="输入应用名"
                               filterable
                               remote
                               :remote-method="handleAppAutoPrompt">
                        <el-option v-for="item in projectsByName"
                                   :key="item.id"
                                   :label="item.name"
                                   :value="item.name">

                        </el-option>
                    </el-select>

                </el-col>

                <el-col :span="4" :offset="1">
                    <el-input v-model="queryItem.appId" placeholder="输入AppId"></el-input>
                </el-col>

                <el-col :span="4" :offset="1">
                    <el-select v-model="queryItem.orgId" placeholder="选择组织">
                        <el-option v-for="item in orgs"
                                   :key="item.id"
                                   :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </el-col>

                <el-col :span="2" :offset="1">
                    <el-button @click="resetQueryForm">重置</el-button>

                </el-col>

                <el-col :span="2" :offset="1">
                    <el-button @click="onSearch">搜索</el-button>
                </el-col>

            </el-row>
        </div>
        <br/>

        <div>
            <el-table :data="projects" style="width: 100%" empty-text="暂无数据" border fit>

                <el-table-column label="ID" prop="id" align="center" sortable></el-table-column>
                <el-table-column label="应用名称" prop="name" align="center"></el-table-column>
                <el-table-column label="应用ID" prop="appId" align="center"></el-table-column>
                <el-table-column label="所属组织" prop="orgDto.name" align="center"></el-table-column>
                <!--<el-table-column label="负责人" prop="userDto.realName" align="center"></el-table-column>-->

                <el-table-column label="负责人" align="center">
                    <template slot-scope="props">
                        <label v-for="item in props.row.userDtos">{{item.realName + " "}}</label>
                    </template>
                </el-table-column>

                <el-table-column label="操作" align="center">
                    <template slot-scope="props">
                        <!--<i class="el-icon-edit"></i>-->
                        <router-link :to="{ path: '/appSummary', query: { appId: props.row.appId }}"> 详情
                        </router-link>
                    </template>
                </el-table-column>

            </el-table>
        </div>

        <div align='center' style="margin-top: 10px">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-sizes="[10, 20, 30, 40]"
                    :page-size.sync="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    import {mapGetters, mapActions} from 'vuex';
    import ProtoDoc from './ProtoDoc.vue'

    export default {
        components:{
            ProtoDoc
        },

        methods: {

            showFileContent(appId) {
                this.$router.push({'path': '/apidocuments', query: { appId: appId }});
            },

            //根据输入部分appName值异步自动弹出以其开头的appName
            handleAppAutoPrompt(data) {
                console.log("data = " + data);
                if (data !== '') {
                    this.$store.dispatch('queryProjectsByAppName', {appName: data});
                }
            },

            //根据输入值异步自动弹出appName
            handleAppAutoPromptAppId(data) {
                console.log("data = " + data);
                if (data !== '') {
                    this.$store.dispatch('queryAppsByAppId', {appId: data});
                }
            },

            //根据输入值异步自动弹出userName
            handleUserAutoPrompt(data) {
                console.log("data = " + data);
                if (data !== '') {
                    //this.appLoading = true;
                    this.$store.dispatch('queryUsersByUserName', {userName: data});
                }
            },

            handleEdit(projectDto) {
                this.dialogVisible = true;
                this.inEditProject = {
                    id: projectDto.id,
                    name: projectDto.name,
                    galaxy: projectDto.galaxy,
                    description: projectDto.description
                };
            },
            handleDelete(projectId) {
                this.$confirm('确认删除？')
                    .then(() => {
                        let data = {
                            projectId: projectId,
                            pageSize: this.pageSize,
                            page: this.currentPage,
                        };
                        this.$store.dispatch('deleteProjectById', data);
                    })
                    .catch(() => {
                    });
            },
            handleSizeChange(data) {
                this.pageSize = data;
                this.$store.dispatch('fetchAllProjects', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                });
            },
            handleCurrentChange(data) {
                this.currentPage = data;
                this.$store.dispatch('fetchAllProjects', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                });
            },
            addProject() {
                console.log(this.inEditProject);
                this.inEditProject = {
                    id: null,
                    name: '',
                    galaxy: '',
                    description: ''
                };
                this.dialogVisible = true;
            },
            onSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let self = this;
                        this.dialogVisible = false;
                        let data = {
                            newProject: self.inEditProject,
                            pageSize: this.pageSize,
                            page: this.currentPage,
                        };
                        if (data.newProject.id == null) {
                            this.$store.dispatch('createNewProject', data);
                        } else {
                            this.$store.dispatch('updateProject', data);
                        }
                    } else {
                        return false;
                    }
                });
            },
            onClose() {
                this.dialogVisible = false;
            },
            onSearch() {

                this.currentPage = 1;
                this.$store.dispatch('queryProjects', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    queryApp: this.queryItem
                });
            },
            resetForm() {
                this.inEditProject = {
                    id: null,
                    name: '',
                    galaxy: '',
                    description: ''
                }
            },
            resetQueryForm() {
                //this.currentValue = ''
                this.queryItem = {
                    appId: null,
                    appName: '',
                    orgId: null,
                    userWorkNumber: ''
                }
            }
        },

        data: function () {
            return {
                dialogVisible: false,

                queryItem: {
                    appId: null,
                    appName: '',
                    orgId: null,
                    userWorkNumber: ''
                },

                currentPage: 1,
                pageSize: 10,

                rules: {
                    name: [
                        {required: true, message: '名字不能为空', trigger: 'blur'}
                    ]
                }
            }
        },

        computed: {
            ...mapGetters({
                projects: 'getAllProjects',
                total: 'getTotalProjects',
                projectsByName: 'getProjectsByName',
                usersByName: 'getUsersByName',
                orgs: 'getAllOrgs',
            })
        },
        created() {
            this.$store.dispatch('fetchAllProjects', {
                pageSize: this.pageSize,
                page: this.currentPage,
            });
            this.$store.dispatch('fetchAllOrgs', {
                pageSize: this.pageSize,
                page: this.currentPage,
            });
        }

    }

</script>

<style>
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
</style>


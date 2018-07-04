<template>

    <div>
        <div class="editFileToolBar">
            <el-button type="small" @click="saveModifiedProtoFile">保存内容</el-button>
            <el-button type="small" @click="returnBack">返回</el-button>
        </div>

        <div class="editFileContent" style="position:absolute;top:40px;bottom: 20px;right: 20px;left: 0;">
            <textarea style="height: 100%;width: 100%;padding: 10px;font-size: 16px;line-height: 1.5"
                      v-model="inEditFileContent">gfdgd</textarea>
        </div>
    </div>
</template>

<script>
    import {mapGetters, mapActions} from 'vuex';
    import $ from "jquery";

    export default {
        data: function () {
            return {
                currentPage: 1,
                pageSize: 10,
                //appId: '',
                inEditFileContent: ''
            };
        },
        methods: {
            saveModifiedProtoFile() {
                let editContract = {
                    name: this.protoFile.name,
                    appid: this.$route.query.appId,
                    content: this.inEditFileContent,
                    //status: 0,
                    //isActive: true
                };

                let data = {
                    appId: this.$route.query.appId,
                    newContract: editContract
                };
                //todo, 更新当前 file
                this.$store.dispatch('addNewContract', data);
                this.goBack()
            },

            //返回高亮展示页面
            returnBack() {
                this.goBack();
            },
            goBack () {
                window.history.length > 1
                    ? this.$router.go(-1)
                    : this.$router.push('/')
            },

            checkProtoFile() {
                let editContract = {
                    name: this.fileName,
                    appid: this.appId,
                    content: this.inEditFileContent,
                    status: 0,
                    isActive: true
                };
                let data = {
                    appId: this.appId,
                    newContract: editContract
                };
                this.$store.dispatch('checkEditProtoContent', data);
            }
        },

        computed: {
            ...mapGetters({
                protoFile: 'getProtoFile',
            }),

            profileContent() {
                if (this.protoFile != null && this.protoFile.content != null) {
                    return this.protoFile.content;
                } else {
                    return '';
                }
            }
        },

        watch: {
            //监控 profileContent, 参数 newProfileContent 为随时更新的值
            profileContent: function (newProfileContent) {
                this.inEditFileContent = newProfileContent;
            }
        },

        created() {

            //说明是 add proto
            if (this.$route.query.newFlag) {
                console.log("add new proto");
                this.$store.dispatch('findProtoFileByFileId', {"fileId": this.$route.query.fileId, "revision": "draft"});
            } else {
                if (this.protoFile != null && this.protoFile.content != null) {
                    this.inEditFileContent = this.protoFile.content;
                } else {
                    console.log("not exists")
                    this.$store.dispatch('findProtoFileByFileId', {"fileId": this.$route.query.fileId,"revision": "draft"});
                    this.inEditFileContent = '';
                }

            }
        },

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

    .editFileToolBar {
        /*//float: right;*/
        margin-bottom: 5px;
    }

</style>


<template>
    <div style="height:100%;width:100%;">
        <el-row class="tool-bar">
            <v-tool-bar :appId="this.appId"></v-tool-bar>
        </el-row>
        <div id='document'
             style=" overflow-y: auto;margin-left: 20px;position: absolute;top: 50px;right: 0;left:0;bottom: 0">
            <!--没有文档显示的内容-->
            <div v-if="nothingToShow">
                该文件没有可展示的文档,请查看其他文件的文档.
            </div>

            <div v-if="! nothingToShow">
                <!--整个document-->
                <div v-for="(swaggerJson,index0) in swaggerJsons" style="margin-right: 20px;">
                    <!--每个swagger 的 name-->
                    <div style="font-size: 30px" :id="index0">{{swaggerJson.info.title}}</div>
                    <!--遍历swagger 展现每个swagger 的各个path-->
                    <div style="margin-bottom: 50px;margin-top:20px" v-for="(path,name,index1) in swaggerJson.paths">
                        <!-- 遍历path 现在 path:pathItem 基本上是一对一的  -->
                        <div :id="index0+''+index1" v-for="(pathItem,method,index2) in path ">
                            <!--展现pathItem-->
                            <a :href="'#'+index0+''+index1"></a>
                            <el-tabs value="first">
                                <el-tab-pane name="first">
                                    <span slot="label"> <span :class="['method', method]">{{method}}</span> {{getRpcName(index0,index1)}} </span>
                                    <path-model :name="name" :pathItem="pathItem"></path-model>
                                    <parameter-model :pathItem="pathItem"
                                                     :components="swaggerJson.components"></parameter-model>
                                    <response-model :pathItem="pathItem"
                                                    :components="swaggerJson.components"></response-model>
                                </el-tab-pane>
                            </el-tabs>
                        </div>
                    </div>
                </div>
            </div>


        </div>
    </div>
</template>

<script>
    import {mapGetters} from 'vuex';

    import vToolBar from '../layout/ToolBar.vue'
    import PathModel from './PathModel.vue'
    import ParameterModel from './ParameterModel.vue'
    import ResponseModel from './ResponseModel.vue'

    export default {
        components: {
            vToolBar,
            PathModel,
            ParameterModel,
            ResponseModel
        },

        data: function () {
            return {
                stopIndex: '04',
                revisionNumber: 'draft',
            };
        },

        computed: {
            ...mapGetters({
                appId: 'getAppId',
                swaggerJsons: 'getSwaggerJsons',
                navigateTreeMeta: 'getNavigateTreeMeta',
            }),
            fileId: function () {
                return this.$route.query.fileId;
            },
            nothingToShow() {
                return !(this.swaggerJsons && this.swaggerJsons.length > 0);
            }

        },
        methods: {
            getRpcName(index1, index2) {
                console.log('getRpcName');
                for (let file of this.navigateTreeMeta) {

                    if (this.fileId == file.protoId) {
                        return file.children[index1] && file.children[index1].children[index2] && file.children[index1].children[index2].label;
                    }
                }
            }
        },
        created() {
            console.log('document created');
            let fileId = this.$route.query.fileId;
            this.stopIndex = this.$route.query.stopIndex;
            let revisionNumber = this.$route.query.revision;

            this.$store.dispatch('findSwaggerJsons', {
                appId: this.appId,
                fileId: fileId,
                revision: revisionNumber || this.revisionNumber
            })
        }

    };

</script>

<style scoped>

    th {
        background-color: #f5f7fa !important;
    }

    .method-type {
        float: right;
        padding: 0px 0px 5px 0px;
    }

    .method {
        text-transform: uppercase;
        text-decoration: none;
        color: white;
        font-size: 0.7em;
        text-align: center;
        padding: 7px;
        margin-right: 5px;
        -moz-border-radius: 2px;
        -webkit-border-radius: 2px;
        -o-border-radius: 2px;
        -ms-border-radius: 2px;
        -khtml-border-radius: 2px;
        border-radius: 2px;
    }

    .delete {
        background-color: #a41e22;
    }

    .get {
        background-color: #0f6ab4;
    }

    .head {
        background-color: #ffd20f;
    }

    .options {
        background-color: #0f6ab4;
    }

    .patch {
        background-color: #D38042;
    }

    .post {
        background-color: #10a54a;
    }

    .put {
        background-color: #c5862b;
    }

</style>


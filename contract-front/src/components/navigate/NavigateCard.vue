<template>
    <el-card body-style="padding:0;font-size:14px;height: 100%" style="height: 100%">
        <div slot="header" class="clearfix">
            <el-row style="display:flex;align-items: center;">
                <el-col :span="2">
                    <i class="el-icon-news"></i>
                </el-col>
                <el-col :span="22">
                    <p @click="showAppInfo" class="AppNameTitle" style="">&nbsp;{{this.currentApp != null ? this.currentApp.name : ''}}</p>
                </el-col>

            </el-row>
        </div>
        <el-input placeholder="输入关键字进行过滤"
                  size="small"
                  prefix-icon="el-icon-search"
                  v-model="filterText">
        </el-input>
        <div style="position: absolute;bottom: -1px;top: 100px;right: 0;left: 1px;">
            <el-tree :data="this.navigateTreeMeta"
                     :props="defaultProps"
                     style="width: 100%;height: 100%"
                     @node-click="handleNodeClick"
                     default-expand-all
                     highlight-current
                     :expand-on-click-node="false"
                     nodeKey="id"
                     :filter-node-method="filterNode"
                     ref="protoTree">
            <span class="custom-tree-node" slot-scope="{ node, data }">
                <span :class="['type-'+node.data.type,'status-'+node.data.status]">
                    <i v-if="node.data.type==='proto'" class="el-icon-document"></i>
                    <i v-if="node.data.type==='service'" class="el-icon-goods"></i>
                    <i v-if="node.data.type==='method'" class="el-icon-location-outline"></i>
                    {{node.label}}
                </span>
                <span class="class-summary"> {{node.data == null ? '': node.data.summary}}</span>
            </span>
            </el-tree>
        </div>
    </el-card>
</template>

<script>
    import {mapGetters} from 'vuex';

    export default {
        watch: {
            filterText(val) {
                this.$refs.protoTree.filter(val);
            }
        },
        data() {
            return {
                filterText: '',
                expandedKeys: [0],
                defaultProps: {
                    children: 'children',
                    label: 'label'
                }
            }
        },
        prots: ['appInfo'],
        computed: {
            ...mapGetters({
                navigateTreeMeta: 'getNavigateTreeMeta',
                showType: 'getShowType',
                appId: 'getAppId',
                currentApp: 'getProjectsByAppId',
                revisionNumber:'getRevisionNumber'
            }),
        },
        methods: {
            filterNode(value, data) {
                if (!value) return true;
                return data.label.indexOf(value) !== -1;
            },
            getIndex(data) {
                console.log('getIndex' + data.id);
                let index = 0;
                for (let i = 0; i < this.navigateTreeMeta.length; i++) {
                    let protoFile = this.navigateTreeMeta[i];
                    const children = protoFile.children || [];
                    for (let j = 0; j < children.length; j++) {
                        let child = children[j];
                        if (data === child) {
                            return '' + j;
                        }
                        for (let k = 0; k < child.children.length; k++) {
                            if (data === child.children[k]) {
                                console.log('getIndex:' + j + k);

                                return '' + j + k;
                            }
                        }
                    }

                }
                return index;
            },

            showAppInfo() {
                //alert("hello")
                this.$router.push({path: '/appSummary', query: {appId: this.$route.query.appId}})
            },

            handleNodeClick(data) {
                //debugger
                console.log("node click data: " + data.label);
                var appId = this.$route.query.appId;

                if (data.id != 0) {
                    //中英文以空格分割
                    let index = data.label.indexOf(' ');
                    let temStop = data.label.substring(0, index);
                    if (index == -1) {
                        temStop = data.label;
                    }
                    console.log("stopAnchor: " + temStop);

                    let stopIndex = this.getIndex(data);
                    this.$router.push({
                        'path': '/content',
                        query: {
                            fileId: data.protoId,
                            appId: appId,
                            stopAnchor: temStop,
                            stopIndex: stopIndex,
                            revision:this.revisionNumber
                        }
                    });

                }
                this.$store.commit("updateCurrentFileId", data.protoId);
                if (this.showType === 'file') {
                    this.$store.dispatch('findProtoFileByFileId', {fileId: data.protoId,revision:this.revisionNumber});
                } else {
                    this.$store.dispatch('findSwaggerJsons', {fileId: data.protoId,revision:this.revisionNumber,appId:this.appId})
                }

            }
        },
        created() {
            if (this.$route.query.apiId) {
                this.expandedKeys = [this.$route.query.apiId]
            }
        }

    }
</script>

<style>
    .AppNameTitle{
        cursor: pointer;
        font-size: 18px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
    .el-input.is-active .el-input__inner, .el-input__inner:focus {
        border-color:  	#A9A9A9;
        outline: 0;
    }

    .el-tree {
        width: 100%;
        overflow: auto;
    }

    .el-tree > .el-tree-node {
        display: inline-block !important;
    }

    .el-tree-node > .el-tree-node__content {
        min-width: 200px;
    }

    .type-proto {
        font-weight: bold;
    }

    .type-service {
        font-weight: bold;
    }

    .type-method {
    }

    .status-0 {
        color: #EE4000
    }

    .status-0 {
        color: #6959CD
    }

    .status-undefined {
        color: #6959CD
    }

    .class-summary {
        color: #080;
        padding-left: 10px;
    }
</style>
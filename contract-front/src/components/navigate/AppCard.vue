<template>
    <el-card class="box-card">

        <div slot="header" class="clearfix" style="background-color: #DCDFE6">
            <i class="el-icon-news"></i>
            <span>应用信息</span>
        </div>
        <div class="text item">
            <label>应用名字：</label>{{appObject.name}}
        </div>
        <div class="text item">
            <label>appId：</label>{{appObject.appId}}
        </div>
        <div class="text item">
            <label>负责人：</label>{{appObject.createdBy}}
        </div>
    </el-card>
</template>


<script>
    import {mapGetters} from 'vuex';

    export default {
        prots: ['appInfo'],
        computed: {
            appObject() {
                let obj = {
                    name: '',
                    appId: '',
                    createdBy: ''
                };
                return this.appInfo == null ? obj : this.appInfo;
            },
            ...mapGetters({
                appInfo: 'getProjectsByAppId'
            }),
        },

    }
</script>


<style>
    .treeview .el-tree-node__content {
        margin: 7px 0;
    }
    .clearfix:before,
    .clearfix:after {
        display: table;
        content: "";
    }
    .clearfix:after {
        clear: both
    }
</style>

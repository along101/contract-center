<template>
    <div>
        <el-tabs value="doc" class="common-absolute">
            <el-tab-pane label="ProtoBuf编写指南" name="doc" style="overflow-y: scroll">
                <proto-doc class="home-tag"></proto-doc>
            </el-tab-pane>
            <el-tab-pane label="项目列表" name="project" style="overflow-y: scroll">
                <project-list class="home-tag"></project-list>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>
<script>
    import ProjectList from "./ProjectList.vue";
    import ProtoDoc from "./ProtoDoc.vue";

    export default {
        components: {ProjectList, ProtoDoc}
    }
</script>

<style>
    .home-tag{
        position: absolute;
        left: 0;
        right: 0;
        top: 50px;
        bottom: 0;
        overflow-y: auto;
    }
    .el-tabs__content{
        position:static !important;

    }
</style>
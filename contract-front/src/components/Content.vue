<template>
    <el-tabs v-model:value="showType" class="common-absolute">
        <el-tab-pane label="文档" name="doc" style="overflow-y: scroll">
            <document></document>
        </el-tab-pane>
        <el-tab-pane label="Protobuf" name="file">
            <proto-content></proto-content>
        </el-tab-pane>
    </el-tabs>

</template>

<script>
    import Document from './document/Document.vue'
    import ProtoContent from './ProtoContent.vue'


    export default {
        components: {
            Document,
            ProtoContent
        },
        beforeRouteUpdate(to, from, next) {
            if(this.showType == 'doc'){
                console.log('beforeRouteUpdate');
                this.$nextTick(() =>  this.scrollCodeView());
            }
            next()
        },
        methods:{
            scrollCodeView() {
                let node = $("#" + this.$route.query.stopIndex + "");
                if (node.length > 0) {
                    let protoCodeView = $("#document");
                    protoCodeView.animate({
                        scrollTop: protoCodeView.scrollTop() + node.position().top
                    }, 500);
                }
            },
        },

        computed: {
            showType: {
                get() {
                    return this.$store.getters.getShowType;
                },
                set(value) {
                    this.$store.commit('setShowType', value);
                }
            }
        }
    }


</script>

<style>
    .el-tabs__content{
        position:static !important;

    }

</style>
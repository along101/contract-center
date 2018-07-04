<template>
    <div style="height:100%;width:100%;">
        <el-row class="tool-bar">
            <v-tool-bar :appId="this.appId"></v-tool-bar>
        </el-row>
        <div style="position: absolute;top:45px;bottom: 0;left: 0;right: 0">
            <pre id="protoCodeView" class="prettyprint lang-proto linenums"
                 style="font-size: 15px;line-height: 1.5;overflow: auto;height: 100%;padding-left: 50px;border-radius:4px;"
                 v-html="this.fileContent">
            </pre>
        </div>
    </div>
</template>

<script>
    import {mapGetters, mapActions} from 'vuex';
    import vToolBar from './layout/ToolBar.vue'
    import $ from "jquery";

    export default {
        components: {
            vToolBar
        },
        data: function () {
            return {
                currentPage: 1,
                pageSize: 10,
                appId: '',
            };
        },
        methods: {
            scrollCodeView(word) {
                let node = $("#protoCodeView span:contains('" + word + "')");
                if (node.length > 0) {
                    let protoCodeView = $("#protoCodeView");
                    protoCodeView.animate({
                        scrollTop: protoCodeView.scrollTop() + node.position().top
                    }, 500);
                }
            }
        },
        computed: {
            ...mapGetters({
                protoFile: 'getProtoFile',
                revision:'getRevisionNumber'
            }),

            fileId() {
                return this.$route.query.fileId;
            },

            stopAnchor() {
                return this.$route.query.stopAnchor;
            },
            fileContent() {
                let fileStr = this.protoFile != null ? this.protoFile.content : '';
                let prettyCode = prettyPrintOne(fileStr, "proto", true);
                if (this.stopAnchor != '') {
                    console.log("anchor: " + this.stopAnchor);
                    this.$nextTick(() => this.scrollCodeView(this.stopAnchor));
                }
                return prettyCode;
            }
        },
        created() {
            console.log('protoContent created')
            //route query 内容不能写到这里，写到 computed 里面
            let revisionNumber = this.$route.query.revision;

            this.$store.dispatch('findProtoFileByFileId', {"fileId": this.fileId,revision:revisionNumber});
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


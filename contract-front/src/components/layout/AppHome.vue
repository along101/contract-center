<template>
    <div>
        <v-api-side-bar class="api-side-bar"></v-api-side-bar>
        <div class="app-home-content">
            <router-view></router-view>
        </div>
    </div>
</template>

<script>
    import {mapGetters} from 'vuex';

    import vApiSideBar from './ApiSideBar.vue';
    import vToolBar from './ToolBar.vue'
    //import ElRow from "element-ui/packages/row/src/row";

    export default {

        components: {

            vApiSideBar,
            vToolBar
        },
        data: function () {
            return {
                fileId: null,
            };
        },

        computed: {
            ...mapGetters({
                protoFile: 'getProtoFile',
                navigateTreeMeta: 'getNavigateTreeMeta',
                projectByAppId: 'getProjectsByAppId',
                revisionNumber: 'getRevisionNumber',
                userName: 'getUserName',
                appInfo: 'getProjectsByAppId',
                isAppAdmin: 'isAppAdmin'
            }),
            fileContent() {
                return this.protoFile != null ? this.protoFile.content : '';
            },
            appId() {
                return this.$route.query.appId;
            },
            fileName() {
                let tempName = this.$route.query.fileName;
                console.log("param fileName in AppHome: " + tempName);
                return tempName;
            },
        },
        mounted() {
            let urlRevision = this.$route.query.revision;

            this.$store.dispatch('fetchProjectByAppId', {"appId": this.appId}).then(() => {
                    if (this.isAppAdmin) {
                        this.$store.commit('updateRevisionNumber', urlRevision || 'draft');
                    } else {
                        if (urlRevision) {
                            this.$store.commit('updateRevisionNumber', urlRevision);
                        } else {
                            this.$store.dispatch('checkoutLatestVersion', this.appId);
                        }
                    }
                }
            );


        },
        created() {
            this.$store.commit("updateAppId", this.appId)
        }

    }

</script>
v
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

    .api-side-bar {
        position: absolute;
        width: 300px;
        left: 0;
        top: 0;
        bottom: 0;
    }

    .app-home-content {
        position: absolute;
        left: 310px;
        right: 0;
        top: 0;
        bottom: 0;
    }

    .tool-bar {
        position: absolute;
        left: 0;
        top: 0;
        right: 0;
    }

    .contract-content {
        position: absolute;
        left: 0;
        top: 40px;
        right: 0;
        bottom: 0;
    }

    .common-absolute {
        position: absolute;
        left: 0;
        right: 0;
        top: 0;
        bottom: 0;
    }
</style>


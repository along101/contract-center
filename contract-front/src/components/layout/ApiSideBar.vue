<template>
    <div>
        <v-navigate-card></v-navigate-card>
    </div>
</template>

<script>
    import {mapGetters} from 'vuex';

    import vAppCard from '../navigate/AppCard.vue'
    import vNavigateCard from '../navigate/NavigateCard.vue'

    export default {
        components: {
            vAppCard, vNavigateCard
        },
        computed:{
            ...mapGetters({
                revision:"getRevisionNumber"
            }),
            appId() {
                return this.$route.query.appId;
            },
        },
        watch:{
            revision:function(newRevision, oldRevision){
                this.$store.dispatch('findNavigateTreeMeta', {
                    "appId": this.appId,
                    revisionNumber: newRevision
                });
            }
        }

    }
</script>

<style>
    .show_box {
        padding: 20px;
    }

    .treeview .el-tree-node__content {
        margin: 3px 0;
    }

    .el-card__header {
        background-color: #DCDFE6;
    }
</style>
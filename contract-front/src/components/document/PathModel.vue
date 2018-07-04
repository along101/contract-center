<template>
    <table class="el-table">
        <colgroup>
            <col width="200">
            <col width="200">
            <col width="200">
        </colgroup>
        <tr>
            <th><div class="cell">路径</div></th>

            <td colspan="3">{{name}}</td>
        </tr>
        <tr>
            <th><div class="cell">概述</div></th>
            <td colspan="3">{{ pathItem.summary || ''}}</td>
        </tr>
        <tr>
            <th><div class="cell">详细描述</div></th>
            <td colspan="3">{{ pathItem.description || ''}}</td>
        </tr>
    </table>
</template>


<script>
    import {mapGetters, mapActions} from 'vuex';

    export default {
        props: ['name', 'pathItem'],
    }
</script>

<style scoped>
     table{
         border-collapse: collapse;
     }
     td{
         border-right-width: unset  !important;;
     }
     td, th{
         border: 1px solid #ebeef5;
         border-collapse: collapse;
     }
    .el-table::before{
        height: 0px;
    }
    .cell{
        color: #909399;
    }

</style>
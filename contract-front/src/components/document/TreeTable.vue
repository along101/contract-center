<template>
    <el-table
            :data="data"
            border
            style="width: 100%"
            :row-style="showTr">
        <el-table-column v-for="(column, index) in columns" :key="column.dataIndex"
                         :label="column.text">
            <template slot-scope="scope">
                <span v-if="spaceIconShow(index)" v-for="(space, levelIndex) in scope.row._level" class="ms-tree-space"></span>
                <button class="button is-outlined is-primary is-small" v-if="toggleIconShow(index,scope.row)" @click="toggle(scope.$index)">
                    <i v-if="!scope.row._expanded" class="el-icon-caret-right" aria-hidden="true"></i>
                    <i v-if="scope.row._expanded" class="el-icon-caret-bottom" aria-hidden="true"></i>
                </button>
                <span v-else-if="index===0" class="ms-tree-space"></span>
                {{scope.row[column.dataIndex]}}
            </template>
        </el-table-column>
    </el-table>
</template>
<script>
    import Vue from 'vue'
    export default {
        name: 'tree-table',
        props: {
            // 该属性是确认父组件传过来的数据是否已经是树形结构了，如果是，则不需要进行树形格式化
            treeStructure: {
                type: Boolean,
                default: function () {
                    return false
                }
            },
            // 这是相应的字段展示
            columns: {
                type: Array,
                default: function () {
                    return []
                }
            },
            // 这是数据源
            dataSource: {
                type: Array,
                default: function () {
                    return []
                }
            },
            // 这个作用是根据自己需求来的，比如在操作中涉及相关按钮编辑，删除等，需要向服务端发送请求，则可以把url传过来
            requestUrl: {
                type: String,
                default: function () {
                    return ''
                }
            },
            // 这个是是否展示操作列
            treeType: {
                type: String,
                default: function () {
                    return 'normal'
                }
            },
            // 是否默认展开所有树
            defaultExpandAll: {
                type: Boolean,
                default: function () {
                    return true
                }
            }
        },
        data () {
            return {}
        },
        computed: {
            // 格式化数据源
            data: function () {
                let me = this;
                if (me.treeStructure) {
                      function treeToArray(data, parent, level, expandedAll) {
                        let tmp = [];
                        Array.from(data).forEach(function (record) {
                            if (record._expanded === undefined) {
                                Vue.set(record, '_expanded', expandedAll)
                            }
                            if (parent) {
                                Vue.set(record, '_parent', parent)
                            }
                            let _level = 0;
                            if (level !== undefined && level !== null) {
                                _level = level + 1
                            }
                            Vue.set(record, '_level', _level);
                            tmp.push(record);
                            if (record.children && record.children.length > 0) {
                                let children = treeToArray(record.children, record, _level, expandedAll)
                                tmp = tmp.concat(children)
                            }
                        });
                        return tmp
                    };

                    let data = treeToArray(me.dataSource, null, null, me.defaultExpandAll)
                    console.log(data);
                    return data
                }
                return me.dataSource
            }
        },
        methods: {
            // 显示行
            showTr: function ({row, rowIndex}) {
                let obj = {row, rowIndex}
                let show = (obj.row._parent ? (obj.row._parent._expanded && obj.row._parent._show) : true)
                obj.row._show = show
                return show ? '' : 'display:none;'
            },
            // 展开下级
            toggle: function (trIndex) {
                console.log('toggle')
                let me = this;
                let record = me.data[trIndex];
                record._expanded = !record._expanded
            },
            // 显示层级关系的空格和图标
            spaceIconShow (index) {
                let me = this;
                if (me.treeStructure && index === 0) {
                    return true
                }
                return false
            },
            // 点击展开和关闭的时候，图标的切换
            toggleIconShow (index, record) {
                let me = this;
                if (me.treeStructure && index === 0 && record.children && record.children.length > 0) {
                    return true
                }
                return false
            },
        }
    }
</script>
<style scoped>
    .ms-tree-space{position: relative;
        top: 1px;
        display: inline-block;
        font-family: 'Glyphicons Halflings';
        font-style: normal;
        font-weight: 400;
        line-height: 1;
        width: 18px;
        height: 14px;}
    .ms-tree-space::before{content: ""}
    table td{
        line-height: 26px;
    }
</style>

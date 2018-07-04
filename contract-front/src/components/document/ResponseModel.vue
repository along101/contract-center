<template>
    <div>
        <div class="title-div">
            <span class="table-title">返回值</span><span class="table-sub-title">{{datasource1[0].name}}</span>
        </div>
        <tree-table :columns="columns" :tree-structure="true" :data-source="datasource1[0].children||[]"></tree-table>
    </div>

</template>


<script>
    import TreeTable from './TreeTable.vue'

    export default {
        components: {TreeTable},
        props: ['pathItem', 'components'],
        computed: {
            parameters: function () {
                return
            }
        },
        data() {
            return {
                columns: [
                    {
                        text: '名称',
                        dataIndex: 'name'
                    },
                    {
                        text: '类型',
                        dataIndex: 'dataType'
                    },
                    {
                        text: '详细描述',
                        dataIndex: 'description'
                    }
                ],
            }
        },
        computed: {
            datasource1: function () {
                console.log('datasource1')
                let list = [];

                //处理response
                if (this.pathItem.responses) {
                    let responses = this.pathItem.responses;
                    let res = this.resolveResponses(responses);
                    list.push(res);
                }
                return list;
            }
        },
        methods: {
            resolveResponses: function (response) {
                console.log('resolve responses')
                let schema = response['200'].content['application/json'].schema;
                let ref = schema.$ref;
                let name = ref.substr(ref.lastIndexOf('/') + 1);
                schema = this.getSchema(name);
                let children = this.getChildren(schema);

                return {
                    name: name,
                    description: response.description,
                    paramType: 'requestBody',
                    dataType: 'Message',
                    children: children,
                }
            },
            getSchema: function (name) {
                return this.components.schemas[name];
            },
            getChildren: function (schema) {
                let requireds = schema.required || [];
                let properties = schema.properties;
                let name1;

                return Object.keys(properties).map((name) => {
                    let property = properties[name];
                    let children = [];
                    if (property.$ref) {
                        var ref = property.$ref;
                        name1 = ref.substr(ref.lastIndexOf('/') + 1);
                        let subSchema = this.getSchema(name1);
                        children = this.getChildren(subSchema);

                    }
                    if ("array" === property.type) {
                        children = this.getArrayChildren(property)
                    }
                    let record = {
                        name: name,
                        required: requireds.indexOf(name) > -1,
                        description: property.description,
                        paramType: 'requestBody',
                        dataType: property.type || name1,
                        children: children
                    };
                    return record;
                });
            },
            getArrayChildren(property) {
                let list = [];
                let items = property.items;
                let name1;
                let children = [];
                if (items.$ref) {
                    var ref = items.$ref;
                    name1 = ref.substr(ref.lastIndexOf('/') + 1);
                    let subSchema = this.getSchema(name1);
                    children = this.getChildren(subSchema);
                } else {
                    children = [{
                        name: '',
                        paramType: 'arrayItem',
                        dataType: items.type || name1,
                        children: children
                    }]
                }

                let record = {
                    name: '',
                    paramType: 'arrayItem',
                    dataType: items.type || name1,
                    children: children
                };

                list.push(record);
                return list;

            }
        }
    }


</script>


<style>
    th {
        background-color: #f5f7fa !important;
    }

    .title-div {
        margin-top: 10px;
        margin-bottom: 3px;
    }

    .table-title-line {
        border: none;
        border-top: 1px solid #ebeef5;
        margin: 10px 0 10px;
    }

    .table-title {
        margin-left: 20px;
        padding: 0 10px;
        font-size: 17px;
        font-weight: bold;
        margin-top: 20px;
        margin-bottom: 5px;
    }

    .table-sub-title {
        margin-left: 20px;
        padding: 0 10px;
        font-size: 13px;
        font-weight: normal;
        margin-top: 10px;
        margin-bottom: 5px;
        color: #909399;
    }
</style>
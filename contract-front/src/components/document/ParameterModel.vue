<template>
    <div>
        <div class="title-div">
            <span class="table-title">请求参数</span><span class="table-sub-title">{{paramName}}</span>
        </div>
        <tree-table :columns="columns" :tree-structure="true" :data-source="datasource1"></tree-table>
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
                        text: '参数位置',
                        dataIndex: 'paramType'
                    },
                    {
                        text: '详细描述',
                        dataIndex: 'description'
                    },

                ],
            }
        },
        computed: {
            paramName: function () {
                if (this.pathItem.requestBody) {
                    let ref = this.pathItem.requestBody.content['application/json'].schema.$ref;
                    let name = ref.substr(ref.lastIndexOf('/') + 1);
                    return name;
                }
            },

            datasource1: function () {
                console.log('datasource1')
                let list = [];
                // 处理param
                if (this.pathItem.parameters) {
                    let paramList = this.pathItem.parameters.map((param) => {
                        return {
                            name: param.name,
                            description: param.description,
                            paramType: param.in,
                            dataType: param.schema.type,
                            required: param.required
                        }
                    });
                    list = list.concat(paramList);
                }


                //处理requestBody
                if (this.pathItem.requestBody) {
                    let reqBody = this.pathItem.requestBody;
                    let req = this.resolveRequestBody(reqBody);
                    list = list.concat(req.children);
                }
                return list;
            }
        },
        methods: {
            resolveRequestBody: function (requestBody) {
                let schema = requestBody.content['application/json'].schema;
                var ref = schema.$ref;
                let name = ref.substr(ref.lastIndexOf('/') + 1);
                schema = this.getSchema(name);
                let children = this.getChildren(schema);

                return {
                    name: name,
                    description: requestBody.description,
                    paramType: 'requestBody',
                    dataType: name,
                    children: children,
                }
            },
            getSchema: function (name) {
                return this.components.schemas[name];
            },
            getChildren: function (schema) {
                let requireds = schema.required || [];
                let properties = schema.properties;

                return Object.keys(properties).map((name) => {
                    let property = properties[name];
                    let children = [];
                    if (property.$ref) {
                        var ref = property.$ref;
                        let name = ref.substr(ref.lastIndexOf('/') + 1);
                        let subSchema = this.getSchema(name);
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
                        dataType: property.type || name,
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
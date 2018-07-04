import {api} from '../../api'

// initial state
const state = {
    protoFile: null,
    currentFileId: 0,
    protoFileList: [],
    fileChange: 0
};

// getters
const getters = {
    getProtoFile: state => state.protoFile,
    getCurrentFileId: state => state.currentFileId,
    getProtoFileListByAppId: state => state.protoFileList,
    getFileChange: state=> state.fileChange,
};

// actions
const actions = {

    //用于页面添加 proto 文件，也可用于页面编辑 proto 文件
    addNewContract({dispatch, commit}, data) {
        return api.protoFileService.addNewContract(data).then(function (res) {
            if (res.data.code == 0) {
                let tmpData = {
                    appId: data.appId,
                    revisionNumber: 'draft'
                };
                console.log("return file id = " + res.data.detail);
                commit("updateCurrentFileId", res.data.detail);
                dispatch("findNavigateTreeMeta", tmpData);
                Promise.resolve(res)
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this))

    },

    //用于页面上传契约文件
    uploadNewProtoFile({dispatch}, data) {
        console.log("上传文件前的appId=" + data.get("appId"));
        api.protoFileService.uploadNewProtoFile(data).then(function (res) {
            if (res.data.code == 0) {
                let tmpData = {
                    appId: data.get("appId"),
                    revisionNumber: 'draft'
                };
                dispatch("queryProtoFileListByAppId", tmpData);
                dispatch("findNavigateTreeMeta", tmpData);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this))

    },

    findProtoFileByFileId({commit}, data) {
        api.protoFileService.readFileContentByFileId(data).then(function (res) {
            if (res.data.code == 0) {
                commit("updateProtoFile", res.data.detail);
            }
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this))
    },

    queryProtoFileListByAppId(context, data) {
        api.protoFileService.queryProtoFileListByAppId(data).then(res => {
            context.commit('updateProtoFileList', res.data.detail);
        }).catch(err => console.log(err))
    },

    deleteProtoFileById(context, data) {
        api.protoFileService.deleteProtoFileById(data.fileId, data.appId).then(res => {
            if (res.data.code == 0) {
                let tmpData = {
                    appId: data.appId,
                    revisionNumber: 'draft'

                };
                context.dispatch("findNavigateTreeMeta", tmpData);
                context.dispatch("queryProtoFileListByAppId", tmpData);
            }
        })
    },

    //删除多个 fileIds
    deleteProtoFileByMultiIds(context, data) {
        api.protoFileService.deleteProtoFileByMultiIds(data).then(res => {
            if (res.data.code == 0) {
                let tmpData = {
                    appId: data.appId,
                    revisionNumber: 'draft'

                };
                context.dispatch("findNavigateTreeMeta", tmpData);
                context.dispatch("queryProtoFileListByAppId", tmpData);
            }
        })
    },

    checkEditProtoContent({commit, dispatch}, data) {
        api.protoFileService.checkProtoContent(data).then(function (res) {
            if (res.data.code == 0) {
                //commit("", res.data.detail);
            }
            dispatch("displayPromptByResponseMsg", res);
        })
    }

};

// mutations
const mutations = {

    updateFileChange(state) {
        state.fileChange = state.fileChange + 1
    },

    updateProtoFile(state, data) {
        //debugger
        state.protoFile = data;
    },

    updateCurrentFileId(state, data) {
        state.currentFileId = data
    },

    updateProtoFileList(state, data) {
        state.protoFileList = data
    }

};

export default {
    state,
    getters,
    actions,
    mutations
}
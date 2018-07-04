import {api} from '../../api'

// initial state
const state = {
    apiDocumentsByAppId: [],
    navigateTreeMeta: [],
    total: 0,
    swaggerJsons: [],
    showType: 'doc'
};

// getters
const getters = {
    getApiDocumentsByAppId: state => state.apiDocumentsByAppId,
    getNavigateTreeMeta: state => {
        if (state.navigateTreeMeta) {
            return state.navigateTreeMeta;
        } else {
            return [];
        }
    },
    getTotal: state => state.total,
    getFilteredDescription: (state) => (text) => {
        return state.apiDescriptionByAppId.filter(desc => {
            return desc.name.indexOf(text) > -1;
        });
    },
    getSwaggerJsons: state => state.swaggerJsons,
    getShowType: state => state.showType
};

// actions
const actions = {
    findApiDocumentsByAppId({commit}, data) {
        api.apiDocumentService.getApiDocumentsByAppId(data).then(function (res) {
            console.log(res.data.detail);
            commit("updateApisList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    findNavigateTreeMeta({commit}, data) {
        api.apiDocumentService.searchNavigateTreeMeta(data).then(function (res) {
            console.log("findNavigateTreeMeta");
            commit('updateRevisionNumber',data.revisionNumber);
            commit("updateNavigateTreeInfo", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this))
    },

    findSwaggerJsons({commit}, data) {
        api.apiDocumentService.findSwaggerJsons(data).then(function (res) {
            commit('updateSwaggerJsons', res.data.detail);
        }).catch(function (err) {
            console.log(err);
        })
    },
};

// mutations
const mutations = {
    updateApisList(state, data) {
        state.apiDocumentsByAppId = data;
        //state.totalProjects = data.totalElements;
    },
    updateApiDescriptionList(state, data) {
        state.apiDescriptionByAppId = data;
    },
    updateNavigateTreeInfo(state, data) {
        state.navigateTreeMeta = data;
    },
    updateSwaggerJsons(state, data) {
        state.swaggerJsons = data;
    },
    changeShowType(state){
        let map = {
            doc:'file',
            file:'doc'
        };
        state.showType = map[state.showType]
    },
    setShowType(state,value){
        console.log('setShowType'+value);
        state.showType = value;
    }

};

export default {
    state,
    getters,
    actions,
    mutations
}
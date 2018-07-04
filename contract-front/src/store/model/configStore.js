import {api} from '../../api'

// initial state
const state = {
    serverUrl:''
};

// getters
const getters = {
    getServerUrl: state => state.serverUrl
};

// actions
const actions = {
    getCommonConfig({commit}) {
        api.configService.getCommonConfig().then(function (res) {
            console.log(res.data);
            commit("updateCommonConfig", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

};

// mutations
const mutations = {
    updateCommonConfig(state,data){
        console.log('updateCommonConfig')
        state.serverUrl = data.serverUrl;
    }
};

export default {
    state,
    getters,
    actions,
    mutations
}
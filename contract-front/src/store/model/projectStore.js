import {api} from '../../api'


// initial state
const state = {
    projects: [],
    projectsByName: [],
    projectsByAppId: null,
    totalProjects: null,
    orgs: [],
    // created 和change appId 的时候需要更新
    appId:null
};

// getters
const getters = {
    getAllProjects: state => state.projects,
    getTotalProjects: state => state.totalProjects,
    getProjectsByName: state => state.projectsByName,
    getProjectsByAppId: state => state.projectsByAppId,
    getAllOrgs: state => state.orgs,
    getAppId:state => state.appId
};

// actions
const actions = {

    fetchAllProjects({commit}, data) {
        api.projectService.getAllProjects(data).then(function (res) {
            console.log(res.data.detail);
            commit("updateProjectsList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    queryProjects({commit}, data) {
        api.projectService.searchProjects(data).then(function (res) {
            console.log(res.data.detail);
            commit("updateProjectsList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    //模糊查询appName
    queryProjectsByAppName({commit}, data) {
        console.log("queryAppsByAppName, data=" + data);
        api.projectService.searchProjectsByAppName(data).then(function (res) {
            commit("updateProjectsByNameList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    fetchAllOrgs({commit}, data) {
        api.projectService.getAllOrgs(data).then(function (res) {
            console.log(res.data.detail);
            commit("updateOrgsList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    fetchProjectByAppId({commit}, data) {
        return api.projectService.getProjectByAppId(data).then(function (res) {
            console.log(res.data.detail);
            commit("updateProjectByAppId", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    }

};

// mutations
const mutations = {
    updateProjectsList(state, data) {
        state.projects = data.content;
        state.totalProjects = data.totalElements;
    },

    updateProjectsByNameList(state, data) {
        state.projectsByName = data;
        //state.totalApps = data.totalElements;
    },
    updateOrgsList(state, data) {
        state.orgs = data;
    },
    updateProjectByAppId({commit}, data) {
        state.projectsByAppId = data;
    },
    updateAppId(state, appId) {
        state.appId = appId;
    }

};

export default {
    state,
    getters,
    actions,
    mutations
}
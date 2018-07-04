import {api} from '../../api'


const state = {
    revisionNumber: "无发布版本",
    releaseHistoryList: [],
    releaseHistory: {}
};


const getters = {
    getRevisionNumber: state => state.revisionNumber,
    getReleaseHistoryList: state => state.releaseHistoryList
};

const actions = {
    checkoutLatestVersion(context, appId) {
        return api.versionService.getLatestReleaseHistory(appId).then(function (res) {
            if (res && res.data && res.data.detail && res.data.detail.revisionNumber){
                context.dispatch("checkoutVersionNumber", {
                    revisionNumber: res.data.detail.revisionNumber,
                    appId: appId
                })
            }else{
                context.dispatch("checkoutVersionNumber", {
                    revisionNumber:"无发布版本",
                    appId: appId
                })
            }
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },
    checkoutVersionNumber(context, data) {

        let revisionNumber = data.revisionNumber;
        //1. 修改versionStore中的版本号
        context.commit("updateRevisionNumber", revisionNumber);


    },
    queryReleaseHistoryList(context, appId) {
        api.versionService.queryReleasehistoryList(appId).then(function (res) {
            context.commit("updateReleaseHistoryList", res.data.detail)
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },
    rollback(context, releaseHistory) {
        api.versionService.rollback(releaseHistory.appid, releaseHistory.id).then(function (res) {
            context.dispatch("checkoutVersionNumber", {revisionNumber: 'draft', appId: releaseHistory.appid})
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },
    changeTag(context,data){
       return api.versionService.changeTag(data.tag, data.id,data.appId);
    }
};

const mutations = {
    updateRevisionNumber(state, data) {
        console.log("updateRevisionNumber");
        state.revisionNumber = data;
    },
    updateReleaseHistoryList(state, data) {
        state.releaseHistoryList = data;
    }
};

export default {
    state,
    getters,
    actions,
    mutations
}


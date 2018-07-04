import restApi from '../restApi'


export default {

    release(appId, tag, ids) {
        let url = '/web/version/release?appId=' + appId + "&tag=" + tag;
        return restApi.doPostRequest(url, ids);
    },

    getLatestReleaseHistory(appId) {
        let url = '/web/version/latestReleaseHistory?appId=' + appId;
        return restApi.doGetRequest(url);
    },

    queryReleasehistoryList(appId) {
        let url = '/web/version/releaseHistoryList?appId=' + appId;
        return restApi.doGetRequest(url);
    },

    rollback(appId, releaseHistoryId) {
        let url = '/web/version/rollback?appId=' + appId + "&releaseHistoryId=" + releaseHistoryId;
        return restApi.doPostRequest(url);
    },

    changeTag(tag, releaseHistoryId,appId) {
        let url = '/web/version/changeTag?appId=' + appId + "&releaseHistoryId=" + releaseHistoryId + "&tag=" + tag;
        return restApi.doPostRequest(url);
    }
}
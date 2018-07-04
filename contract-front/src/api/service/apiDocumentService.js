import restApi from '../restApi'
import {stringify} from 'querystring'

export default {

    getApiMetaByAppId(request) {
        let url = '/web/apidocuments/meta/' + request.appId;
        return restApi.doGetRequest(url);
    },
    searchNavigateTreeMeta(request) {
        let url = '/web/apidocuments/navigate/' + request.appId + "/revision/" + (request.revisionNumber || "draft");
        return restApi.doGetRequest(url);
    },

    findSwaggerJsons(request) {
        let url = '/web/apidocuments/swagger/json?'+stringify(request);
        return restApi.doGetRequest(url);
    }

}

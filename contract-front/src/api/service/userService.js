import restApi from '../restApi'

export default {

    fuzzySearchUsersByName(request) {
        let url = '/web/users/fuzzy?userName=' + request.userName;
        return restApi.doGetRequest(url);
    }


}

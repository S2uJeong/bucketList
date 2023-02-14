axios.interceptors.request.use(function(config) {
    const accessToken = localStorage.getItem('accessToken');
    if(accessToken) {
        config.headers.Authorization = 'Bearer' + ' ' + accessToken
    }
    return config;
})

axios.interceptors.response.use(
    success => success,
    async(error) => {
        const status = error.response.status

        if(status === 401) {
            const originRequest = error.config

            await axios.post('/reissue')
                .then(response => {
                    localStorage.setItem("accessToken", response.data);
                    originRequest.headers.authorization = 'Bearer ' + response.data;
                    return axios(originRequest);
                })
                .catch(error => {
                    localStorage.removeItem('accessToken')
                    window.location.href = '/';
                })
            return Promise.reject(error)
        }
    }
)
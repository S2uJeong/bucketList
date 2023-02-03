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
        console.log(error);
        const status = error.response.status

        if(status === 401) {
            const originRequest = error.config

            await axios.post('/reissue')
                .then(response => {
                    console.log(response.data);
                    localStorage.setItem("accessToken", response.data);
                    originRequest.headers.authorization = 'Bearer ' + response.data;
                    return axios(originRequest);
                })
                .catch(error => {
                    console.log('재발급 실패, refresh token 만료');
                    localStorage.removeItem('accessToken')
                    window.location.href = '/';
                })
            return Promise.reject(error)
        }
    }
)
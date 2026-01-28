import axios from "axios";

export default function (url,method,data){
    return axios({
        baseURL: "http://localhost:8080/api",
        url,
        method,
        data,
      })
}

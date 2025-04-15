import {getAccount} from "@/net/index.js";
import {useStore} from "@/store/index.js";

export const getUserInfo = (loadingRef) => {
    loadingRef.value=true
    const store=useStore()
    getAccount(data=>{
        store.user = data;
        loadingRef.value=false
    })
};
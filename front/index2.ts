import {createStore, Store} from 'vuex'

type Role =  'user'|'admin' ;

// typescript
type State = {
    count: number,
    bgUrl: string,
    userId: string,
    userRole: string
}

// 创建一个新的 store 实例
const store: Store<State> = createStore({
    state(): State {
        return {
            count: 0,
            bgUrl: 'assets/img/bg/bg1.jpg',
            userId: '',
            userRole: '',
        }
    },
    mutations: {
        increment(state: State): void {
            state.count++
        },
        setBg(state: State, url: string) {
            state.bgUrl = url
        },
        resetBg(state: State) {
            state.bgUrl = 'assets/img/bg/bg1.jpg'
        },
        setUserId(state: State, userId: string) {
            state.userId = userId
        },
        setRole(state: State, userRole: string) {
            state.userRole = userRole
        },

    }
})

export default store
export {State}
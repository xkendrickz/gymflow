import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null as null | { name: string; email: string },
    token: null as null | string,
    role: null as null | 'admin' | 'kasir' | 'mo' | 'member',
  }),

  actions: {
    login(
      userData: { name: string; email: string },
      token: string,
      role: 'admin' | 'kasir' | 'mo' | 'member',
    ) {
      this.user = userData
      this.token = token
      this.role = role
      localStorage.setItem('token', token)
      localStorage.setItem('role', role)
      localStorage.setItem('user', JSON.stringify(userData))
    },

    logout() {
      this.user = null
      this.token = null
      this.role = null
      localStorage.removeItem('token')
      localStorage.removeItem('role')
      localStorage.removeItem('user')
    },

    init() {
      const token = localStorage.getItem('token')
      const role = localStorage.getItem('role') as 'admin' | 'kasir' | 'mo' | 'member' | null
      const user = localStorage.getItem('user')
      if (token) this.token = token
      if (role) this.role = role
      if (user) this.user = JSON.parse(user)
    },
  },

  getters: {
    isLoggedIn: (state) => !!state.token,
    isAdmin: (state) => state.role === 'admin',
    isKasir: (state) => state.role === 'kasir',
    isMO: (state) => state.role === 'mo',
  },
})

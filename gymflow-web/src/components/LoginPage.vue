<template>
	<div class="login-wrapper">

		<!-- Left: Form Panel -->
		<div class="form-panel">
			<div class="form-inner">

				<!-- Logo -->
				<div class="logo-area">
					<div class="logo-icon">
						<v-icon color="orange" size="28">mdi-dumbbell</v-icon>
					</div>
					<span class="logo-text">GymFlow</span>
				</div>

				<!-- Heading -->
				<h1 class="login-title">Welcome back.</h1>
				<p class="login-subtitle">Sign in to manage your gym.</p>

				<!-- Error alert -->
				<v-alert v-if="loginError" type="error" variant="tonal" class="mb-4" density="compact" closable
					@click:close="loginError = ''">
					{{ loginError }}
				</v-alert>

				<!-- Form -->
				<form @submit.prevent="login">
					<div class="field-group">
						<label class="field-label">Username</label>
						<v-text-field v-model="pegawai.username" placeholder="Enter your username" variant="outlined"
							density="comfortable" bg-color="#1a1a1a" color="orange"
							:error-messages="validation.username?.[0]" autocomplete="username" />
					</div>

					<div class="field-group">
						<label class="field-label">Password</label>
						<v-text-field v-model="pegawai.password" placeholder="Enter your password"
							:type="showPassword ? 'text' : 'password'" variant="outlined" density="comfortable"
							bg-color="#1a1a1a" color="orange"
							:append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
							@click:append-inner="showPassword = !showPassword"
							:error-messages="validation.password?.[0]" autocomplete="current-password" />
					</div>

					<v-btn type="submit" block size="large" :loading="loading" class="login-btn mt-2" rounded="lg">
						<span v-if="!loading">Let's Go →</span>
					</v-btn>
				</form>

				<!-- Demo credentials -->
				<div class="demo-hint">
					<p class="demo-title">Demo Credentials</p>
					<div class="demo-row">
						<span class="demo-role">Admin</span>
						<code class="demo-cred">admin / admin123</code>
					</div>
					<div class="demo-row">
						<span class="demo-role">Kasir</span>
						<code class="demo-cred">kasir / kasir123</code>
					</div>
				</div>
			</div>
		</div>

		<!-- Right: Image Panel -->
		<div class="image-panel">
			<div class="image-overlay" />
			<div class="image-content">
				<div class="quote-card">
					<v-icon color="orange" size="32" class="mb-3">mdi-format-quote-open</v-icon>
					<p class="quote-text">
						"The resistance that you fight physically in the gym and the resistance that you fight in life
						can only build a strong character."
					</p>
					<p class="quote-author">— Arnold Schwarzenegger</p>
				</div>
			</div>
		</div>

	</div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import api from '@/lib/axios'
import { useToast } from 'vue-toastification'

const router = useRouter()
const auth = useAuthStore()
const toast = useToast()

const pegawai = reactive({ username: '', password: '' })
const validation = ref<Record<string, string[]>>({})
const loginError = ref('')
const loading = ref(false)
const showPassword = ref(false)

onMounted(() => {
	document.title = 'Login — GymFlow'
	window.history.pushState(null, '', window.location.href)
	window.onpopstate = () => window.history.pushState(null, '', window.location.href)
})

async function login() {
	loading.value = true
	loginError.value = ''
	validation.value = {}

	try {
		const response = await api.post('/loginWeb', {
			username: pegawai.username,
			password: pegawai.password,
		})

		if (response?.data) {
			const token = response.data.token
			const data = response.data.data
			auth.login(
				{ name: data.nama_pegawai, email: data.email ?? '' },
				token,
				roleFromId(data.id_role)
			)
			toast.success('Berhasil Login!', { timeout: 2000 })
			const roleRoutes: Record<number, string> = {
				1: 'admin.beranda',
				2: 'admin.beranda',
				3: 'kasir.beranda'
			}
			router.push({ name: roleRoutes[data.id_role] ?? 'login' })
		}
	} catch (error: any) {
		if (error.response?.data?.errors) {
			validation.value = error.response.data.errors
		} else {
			loginError.value = 'Username atau password salah.'
		}
	} finally {
		loading.value = false
	}
}

function roleFromId(id: number): 'admin' | 'kasir' | 'member' {
	const map: Record<number, 'admin' | 'kasir' | 'member'> = {
		1: 'admin', 2: 'admin', 3: 'kasir',
	}
	return map[id] ?? 'member'
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;800&family=Inter:wght@400;500&display=swap');

.login-wrapper {
	display: flex;
	height: 100vh;
	background-color: #0d0d0d;
	font-family: 'Inter', sans-serif;
}

/* ── Form Panel ── */
.form-panel {
	width: 100%;
	max-width: 480px;
	display: flex;
	align-items: center;
	justify-content: center;
	padding: 2rem;
	background-color: #0d0d0d;
	z-index: 1;
}

.form-inner {
	width: 100%;
	max-width: 380px;
}

.logo-area {
	display: flex;
	align-items: center;
	gap: 10px;
	margin-bottom: 2.5rem;
}

.logo-icon {
	width: 44px;
	height: 44px;
	border-radius: 10px;
	background: rgba(251, 146, 60, 0.15);
	border: 1px solid rgba(251, 146, 60, 0.3);
	display: grid;
	place-items: center;
}

.logo-text {
	font-family: 'Montserrat', sans-serif;
	font-weight: 800;
	font-size: 1.4rem;
	color: #ffffff;
	letter-spacing: -0.3px;
}

.login-title {
	font-family: 'Montserrat', sans-serif;
	font-size: 2rem;
	font-weight: 800;
	color: #ffffff;
	line-height: 1.2;
	margin-bottom: 0.5rem;
}

.login-subtitle {
	font-size: 0.95rem;
	color: #6b7280;
	margin-bottom: 2rem;
}

.field-group {
	margin-bottom: 0.25rem;
}

.field-label {
	display: block;
	font-size: 0.8rem;
	font-weight: 600;
	color: #9ca3af;
	text-transform: uppercase;
	letter-spacing: 0.05em;
	margin-bottom: 0.4rem;
}

.login-btn {
	background: linear-gradient(135deg, #f97316, #ea580c) !important;
	color: #ffffff !important;
	font-family: 'Montserrat', sans-serif;
	font-weight: 700;
	font-size: 1rem;
	letter-spacing: 0.3px;
	height: 52px !important;
	box-shadow: 0 4px 20px rgba(249, 115, 22, 0.35) !important;
	transition: all 0.2s ease !important;
}

.login-btn:hover {
	box-shadow: 0 6px 28px rgba(249, 115, 22, 0.55) !important;
	transform: translateY(-1px);
}

/* ── Image Panel ── */
.image-panel {
	flex: 1;
	position: relative;
	display: none;
	background-image: url('https://images.unsplash.com/photo-1534438327276-14e5300c3a48');
	background-size: cover;
	background-position: center;
}

@media (min-width: 1024px) {
	.image-panel {
		display: block;
	}
}

.image-overlay {
	position: absolute;
	inset: 0;
	background: linear-gradient(135deg,
			rgba(13, 13, 13, 0.75) 0%,
			rgba(13, 13, 13, 0.4) 100%);
}

.image-content {
	position: absolute;
	inset: 0;
	display: flex;
	align-items: flex-end;
	padding: 3rem;
}

.quote-card {
	background: rgba(13, 13, 13, 0.6);
	backdrop-filter: blur(12px);
	border: 1px solid rgba(255, 255, 255, 0.08);
	border-radius: 16px;
	padding: 1.75rem 2rem;
	max-width: 420px;
}

.quote-text {
	font-family: 'Montserrat', sans-serif;
	font-size: 1.1rem;
	font-weight: 600;
	color: #f9fafb;
	line-height: 1.6;
	margin-bottom: 0.75rem;
}

.quote-author {
	font-size: 0.875rem;
	color: #f97316;
	font-weight: 500;
}
</style>
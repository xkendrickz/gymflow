<template>
    <div class="page-header">
        <h2 class="page-title">Edit Kelas</h2>
        <p class="page-subtitle">Update data kelas gym</p>
    </div>

    <v-card class="form-card" rounded="xl">
        <v-card-text class="pa-6">
            <div v-if="fetchLoading" class="d-flex justify-center pa-8">
                <v-progress-circular indeterminate color="orange" />
            </div>
            <form v-else @submit.prevent="update">
                <div class="form-grid">
                    <div>
                        <p class="field-label">Nama Kelas</p>
                        <v-text-field v-model="form.nama_kelas" placeholder="Contoh: Yoga, Zumba, Body Combat"
                            variant="outlined" density="comfortable" color="orange" bg-color="#1a1a1a"
                            :error-messages="errors.nama_kelas" />
                    </div>
                    <div>
                        <p class="field-label">Tarif (Rp)</p>
                        <v-text-field v-model="form.tarif" type="number" placeholder="Contoh: 50000" variant="outlined"
                            density="comfortable" color="orange" bg-color="#1a1a1a" prefix="Rp"
                            :error-messages="errors.tarif" />
                    </div>
                </div>
                <v-divider class="my-6" style="border-color: rgba(255,255,255,0.06);" />
                <div class="form-actions">
                    <v-btn variant="outlined" color="grey" rounded="lg"
                        @click="router.push({ name: 'admin.kelas.index' })">Batal</v-btn>
                    <v-btn type="submit" :loading="loading" rounded="lg" class="save-btn">
                        <v-icon start>mdi-content-save</v-icon>Simpan
                    </v-btn>
                </div>
            </form>
        </v-card-text>
    </v-card>
</template>

<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router'
import { useForm } from '@/composables/useForm'

const router = useRouter()
const route = useRoute()
const id = route.params.id

const { form, errors, loading, fetchLoading, fetchData, submit } = useForm({
    nama_kelas: '',
    tarif: '',
})

fetchData(`/kelas/${id}`, data => ({
    nama_kelas: data.nama_kelas,
    tarif: data.tarif,
}))

const update = () =>
    submit('put', `/kelas/${id}`, 'Berhasil Edit Kelas!', 'admin.kelas.index')
</script>
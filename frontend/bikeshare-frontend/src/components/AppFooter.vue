<template>
  <v-footer height="40" app>

    <div
      class="text-caption text-disabled"
      style="position: absolute; right: 16px;"
    >
      Made with Vuetify |
      Developed by Nguyen Huu Duc |
      DAC Data Technology Vietnam 2024 Internship Program
    
    </div>
  </v-footer>
</template>

<script setup>
  
</script>

<style scoped lang="sass">
  .social-link :deep(.v-icon)
    color: rgba(var(--v-theme-on-background), var(--v-disabled-opacity))
    text-decoration: none
    transition: .2s ease-in-out

    &:hover
      color: rgba(25, 118, 210, 1)
</style>

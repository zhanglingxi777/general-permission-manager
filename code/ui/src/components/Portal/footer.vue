<template>
  <footer>
    <p>Copyright © 2022-2023 <strong>linxi blog</strong> All Rights Reserved.</p>
  </footer>
</template>
<script>
export default {
  name: "PortalFooter"
}
</script>
<style scoped>
footer {
  width: 100%;
  height: 40px;
  font: 14px/40px Consolas,Sans-serif;
  color: #999;
  text-align: center;
  border-top: 1px solid #e4e4e4;
  display: block;
}

footer p:nth-child(1) strong {
  font-weight: 500;
}
</style>
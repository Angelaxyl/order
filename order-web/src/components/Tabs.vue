<template>
  <div>

    <el-tabs class="el-tabs-v1" ref="tabs" v-model="editableTabsValue" type="card" :closable="editableTabs.length > 1"
      @edit="handleTabsEdit">
      <el-tab-pane :key="item.name" v-for="(item, index) in editableTabs" :label="item.title" :name="item.name">
        <span style="font-weight:bold;" slot="label"><i style="font-weight:bold;" :class="item.icon"></i>
          {{item.title}}</span>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
  export default {
    name: 'Tabs',
    data () {
      return {
        editableTabsValue: '',
        editableTabs: [],
        tabIndex: 1
      }
    },
    methods: {
      handleTabsEdit (targetName, action) {

        let tabs = this.editableTabs;
        let activeName = this.editableTabsValue;
        if (activeName === targetName) {
          tabs.forEach((tab, index) => {
            if (tab.name === targetName) {
              let nextTab = tabs[index + 1] || tabs[index - 1];
              if (nextTab) {
                activeName = nextTab.name;
              }
            }
          });
        }

        this.editableTabsValue = activeName;
        this.editableTabs = tabs.filter(tab => tab.name !== targetName);
        sessionStorage.setItem('Tabs',JSON.stringify(this.editableTabs))
        if (this.$route.path == this.editableTabsValue) {
          return
        }
        this.$router.push(this.editableTabsValue)

      },

    },
    watch: {
      $route (curVal, oldVal) {
        let isTrue = true
        let tabs = this.editableTabs;
        tabs.forEach((tab, index) => {
          if (tab.name == curVal.path) {
            this.editableTabsValue = curVal.path
            isTrue = false
          }
        });
        if (isTrue) {
          let newTabName = ++this.tabIndex + '';
          this.editableTabs.push({
            title: curVal.name,
            name: curVal.path,
            content: curVal.name,
            icon: this.$route.meta.icon
          });
          this.editableTabsValue = curVal.path;
          // sessionStorage.setItem('Tabs',JSON.stringify(this.editableTabs))
        }
      },
      editableTabsValue (curVal, oldVal) {
        if (this.$route.path == curVal) {
          return
        }
        this.$router.push(curVal)
      }
    },
    mounted () {
      // let tabs = JSON.parse(sessionStorage.getItem('Tabs'))
      // console.log(tabs)
      // if(tabs != null){
      //   this.editableTabs = tabs
      //   this.editableTabsValue = this.$route.path;
      //   return
      // }
      // let newTabName = ++this.tabIndex + '';
      this.editableTabs.push({
        title: this.$route.name,
        name: this.$route.path,
        content: this.$route.name,
        icon: this.$route.meta.icon
      });
      this.editableTabsValue = this.$route.path;
      // sessionStorage.setItem('Tabs',JSON.stringify(this.editableTabs))
    },
  };
</script>


<style>

</style>
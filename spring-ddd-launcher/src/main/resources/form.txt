<script lang="ts" setup>
import { ref } from 'vue';

import { useVbenModal } from '@vben/common-ui';
import { $t } from '@vben/locales';

import { ElMessage } from 'element-plus';

import { useVbenForm, z } from '#/adapter/form';
import { createBind, updateBind } from '#/api/gen/bind/';
import { getItemLabelByDictCode } from '#/api/sys/dict/';

const props = defineProps<{
  gridApi: any;
}>();

const writeForm = ref<Record<string, any>>({});

const [Form, formApi] = useVbenForm({
  showDefaultActions: false,
  layout: 'horizontal',
  commonConfig: {
    componentProps: {
      class: 'w-full',
    },
    colon: true,
    labelWidth: 150,
  },
  submitOnChange: true,
  schema: [
    {
      component: 'Input',
      fieldName: 'columnType',
      label: $t('codegen.bind.columnType'),
      componentProps: {
        placeholder: `${$t('system.common.placeholder.input')} ${$t('codegen.bind.columnType')}`,
        onInput: (e: any) => {
          e.target.value = e.target.value.toLowerCase();
        },
      },
      rules: z
        .string()
        .min(1, { message: $t('system.common.placeholder.input') })
        .regex(/^[a-z]+$/, {
          message: $t('codegen.bind.columnTypeLowercaseOnly'),
        })
        .refine((value) => /^[a-z]+$/.test(value), {
          message: $t('codegen.bind.columnTypeLowercaseOnly'),
        }),
    },
    {
      component: 'Input',
      fieldName: 'entityType',
      label: $t('codegen.bind.entityType'),
      componentProps: {
        placeholder: `${$t('system.common.placeholder.input')} ${$t('codegen.bind.entityType')}`,
      },
      rules: 'required',
    },
    {
      component: 'ApiSelect',
      componentProps: {
        allowClear: true,
        filterOption: true,
        api: getComponentDict,
        showSearch: true,
        labelField: 'itemLabel',
        valueField: 'itemValue',
        clearable: true,
        placeholder: `${$t('system.common.placeholder.input')} ${$t('codegen.bind.componentType')}`,
      },
      fieldName: 'componentType',
      label: $t('codegen.bind.componentType'),
      rules: 'required',
    },
    {
      component: 'ApiSelect',
      componentProps: {
        allowClear: true,
        filterOption: true,
        api: getTypeScriptDict,
        showSearch: true,
        labelField: 'itemLabel',
        valueField: 'itemValue',
        clearable: true,
        placeholder: `${$t('system.common.placeholder.input')} ${$t('codegen.bind.typescriptType')}`,
      },
      fieldName: 'typescriptType',
      label: $t('codegen.bind.typescriptType'),
      rules: 'required',
    },
  ],
});

const [Modal, modalApi] = useVbenModal({
  onConfirm: () => {
    formApi.validate().then(async (e) => {
      if (e.valid) {
        Object.assign(writeForm.value, await formApi.getValues());
        await (writeForm.value.id
          ? updateBind(writeForm.value)
          : createBind(writeForm.value));
        ElMessage.success($t('system.common.save.success'));
        props.gridApi.reload();
      } else {
        ElMessage.error($t('system.common.validation.error'));
      }
      await modalApi.setState({ loading: false }).close();
    });
  },
  confirmText: $t('system.common.button.confirm'),
  cancelText: $t('system.common.button.cancel'),
});

const open = (row: any) => {
  writeForm.value = {};
  if (row?.id) {
    writeForm.value = row;
    formApi.setValues(row);
  } else {
    formApi.resetForm();
  }
  modalApi.open();
};
const close = () => modalApi.close();

defineExpose({ open, close });

function getComponentDict() {
  return getItemLabelByDictCode('components');
}

function getTypeScriptDict() {
  return getItemLabelByDictCode('typescript_type');
}
</script>

<template>
  <Modal class="w-[40%]" :title="$t('system.common.alert.form')">
    <Form style="width: auto" />
  </Modal>
</template>

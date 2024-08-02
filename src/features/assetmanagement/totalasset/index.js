import React, { useState, useEffect } from 'react';
import { Button, Modal, Form, Input, Select, Dropdown, Menu, Tag, Space, Popconfirm, Table, notification } from 'antd';
import { DownOutlined, EditOutlined, EyeOutlined, DeleteOutlined, DownloadOutlined } from '@ant-design/icons';
import { DatePicker } from 'antd'; 
import moment from 'moment';

const { Option } = Select;
const { RangePicker } = DatePicker;





// Define table columns
const columns = (handleEdit, handleDelete, handleViewHide) => [
  {
    title: 'No',
    dataIndex: 'key',
    render:(_,r,index)=>index+1
  },
  {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
    render: (text) => <a>{text}</a>,
  },
  {
    title: 'Category',
    dataIndex: 'categoryId',
    key: 'categoryId',
    render: (_,categoryId) => {
    
      return categoryId.name
      
    },
  },
  {
    title: 'Model',
    dataIndex: 'model',
    key: 'model',
  },
  {
    title: 'Year',
    dataIndex: 'year',
    key: 'year',
  },
  {
    title: 'Serial Number',
    dataIndex: 'serialNumber',
    key: 'serialNumber',
  },
  {
    title: 'Purchase Date',
    dataIndex: 'purchaseDate',
    key: 'purchaseDate',
    render: (date) => date ? new Date(date).toLocaleDateString() : 'N/A',
  },
  {
    title: 'Price',
    dataIndex: 'price',
    key: 'price',
    render: (price) => `$${price.toFixed(2)}`,
  },
  {
    title: 'Unit',
    dataIndex: 'unit',
    key: 'unit',
  },
  {
    title: 'Quantity',
    dataIndex: 'quantity',
    key: 'quantity',
  },
  
  {
    title: 'Action',
    key: 'action',
    render: (_, record) => (
      <Space size="middle">
        <Button 

          icon={<EditOutlined/>} 
          onClick={() => handleEdit(record)}
          className='bg-green-600 hover:bg-green-700 text-white border-none rounded-md p-2 shadow-md'
        />
         <Dropdown 
          overlay={
            <Menu>
              <Menu.Item key="view">
                <Button 
                  type="link" 
                  onClick={() => handleViewHide(record, 'view')}
                >
                  View
                </Button>
              </Menu.Item>
              <Menu.Item key="hide">
                <Button 
                  type="link" 
                  onClick={() => handleViewHide(record, 'hide')}
                >
                  Remove
                </Button>
              </Menu.Item>
            </Menu>
          }
        >
          <Button 
            icon={<EyeOutlined />} 
            className='bg-white hover:bg-yellow-500 text-yellow-500 border-none rounded-full p-2 shadow-md'
          />
        </Dropdown>
      </Space>
    ),
  },
];










const TotalAsset = () => {
  const [size, setSize] = useState('large');
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [modalType, setModalType] = useState(''); // 'fixedasset' or 'category'
  const [editCategory, setEditCategory] = useState(null);
  const [editKey, setEditKey] = useState(null);
  const [form] = Form.useForm();
  const [categories, setCategories] = useState([]);
  const [data, setData] = useState([]);
  const token = localStorage.getItem('token');
  const [visibleAssets, setVisibleAssets] = useState([]);
  const [viewAsset, setViewAsset] = useState(null);


  // Fetch categories and assets on component mount
  useEffect(() => {
    fetchCategories();
    fetchFixedAssets();
  }, []);

  const fetchCategories = async () => {
    try {
      const headers = { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` };

      const response = await fetch('http://localhost:6060/admin/categories',{headers});
      const result = await response.json();
      console.log('Fetched Categories:', result);
      if (result.statusCode === 200) {
        setCategories(result.categories || []);
      } else {
        notification.error({
          message: 'Failed to fetch categories',
          description: result.error,
        });
      }
    } catch (error) {
      console.error('Error fetching categories:', error);
    }
  };

  const fetchFixedAssets = async () => {
    try {
      const headers = { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` };
      console.log(headers);
      const response = await fetch('http://localhost:6060/admin/getAllFixedAssets',{headers});
      const result = await response.json();
      if (result.statusCode === 200) {
        console.log(result.fixedAssets);
        setData(result.fixedAssets || []);
      } else {
        notification.error({
          message: 'Failed to fetch fixed assets',
          description: result.error,
        });
      }
    } catch (error) {
      console.error('Error fetching fixed assets:', error);
    }
  };

  const showModal = (type) => {
    setModalType(type);
    setEditCategory(null);
    setEditKey(null);
    setIsModalVisible(true);
  };

  const handleOk = async () => {
    try {
      const values = await form.validateFields();
      const headers = { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` };

      if (modalType === 'category') {
        if (editCategory) {
          // Edit existing category
          await fetch(`http://localhost:6060/admin/categories/${editCategory.id}`, {
            method: 'PUT',
            headers,
            body: JSON.stringify(values),
          });

          setCategories((prevCategories) =>
            prevCategories.map((cat) =>
              cat.id === editCategory.id ? values : cat
            )
          );

          notification.success({
            message: 'Category Updated',
            description: `Category "${values.name}" has been updated successfully.`,
          });
        } else {
          // Create new category
          const response = await fetch('http://localhost:6060/admin/createCategory', {
            method: 'POST',
            headers,
            body: JSON.stringify(values),
          });

          const result = await response.json();
          setCategories((prevCategories) => [...prevCategories, result.category]);

          notification.success({
            message: 'Category Created',
            description: `Category "${values.name}" has been created successfully.`,
          });
        }
        await fetchCategories();
      } else if (modalType === 'fixedasset') {
        if (editKey !== null) {
          // Edit existing asset
          await fetch(`http://localhost:6060/admin/fixedassets/${editKey}`, {
            method: 'PUT',
            headers,
            body: JSON.stringify(values),
          });

          setData((prevData) =>
            prevData.map((item) =>
              item.key === editKey ? { ...item, ...values } : item
            )
          );

          notification.success({
            message: 'Asset Updated',
            description: 'Fixed asset has been updated successfully.',
          });
        } else {
          // Create new asset
          const headers = { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` };
          const response = await fetch('http://localhost:6060/admin/createFixedAsset', {
            method: 'POST',
            headers,
            body: JSON.stringify(values),
          });

          const result = await response.json();
          setData((prevData) => [...prevData, result.fixedasset]);

          notification.success({
            message: 'Asset Created',
            description: 'A new fixed asset has been created successfully.',
          });
        }
        await fetchFixedAssets();
      }
      form.resetFields();
      setIsModalVisible(false);
    } catch (info) {
      console.log('Validate Failed:', info);
    }
  };


  const handleCancel = () => {
    setIsModalVisible(false);
  };

  const handleEdit = (record) => {
    setModalType('fixedasset');
    setEditKey(record.key);
    form.setFieldsValue(record);
    form.setFieldValue({
      name:record.name,
      categoryId: record.categoryId,
      model:record.model,
      year:record.year,
      serialNumber: record.serialNumber,
      purchaseDate: record.purchaseDate ? moment(record.purchaseDate) : null,
      price: record.price,
      unit:record.unit,
      quantity:record.quantity,
    });
    setIsModalVisible(true)
  };


  const handleDelete = async (record) => {
    console.log('Deleting asset with key:', record.id);
    try {
      const token = localStorage.getItem('token');
      const headers = { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` };
      await fetch(`http://localhost:6060/admin/deleteFixeAsset/${record.id}`, {
        method: 'DELETE',
        headers,
      });
      console.log('Deleting asset with key:', record.key);
      setData((prevData) => prevData.filter((item) => item.id !== record.id));
      notification.success({
        message: 'Asset Deleted',
        description: 'Fixed asset has been deleted successfully.',
      });
    } catch (error) {
      console.error('Error deleting fixed asset:', error);
      notification.error({
        message: 'Failed to delete asset',
        description: error.message,
      });
    }
  };

  const handleCategoryEdit = (category) => {
    setModalType('category');
    setEditCategory(category);
    form.setFieldsValue({
      name: category.name,
      description: category.description,
    });
    setIsModalVisible(true);
  };

  const handleCategoryDelete = async (category) => {
    try {
      const token = localStorage.getItem('token');
      const headers = { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` };
      await fetch(`http://localhost:6060/admin/categories/${category.id}`, {
        method: 'DELETE',
        headers,
      });

      setCategories((prevCategories) => prevCategories.filter((cat) => cat.id !== category.id));

      notification.success({
        message: 'Category Deleted',
        description: 'Category has been deleted successfully.',
      });
    } catch (error) {
      console.error('Error deleting category:', error);
      notification.error({
        message: 'Failed to delete category',
        description: error.message,
      });
    }
  };















  const menu = (
    <Menu>
      <Menu.Item key="1">
        <Button type="link" onClick={() => showModal('fixedasset')}>
          Create Fixed Asset
        </Button>
      </Menu.Item>
      <Menu.Item key="2">
        <Button type="link" onClick={() => showModal('category')}>
          Create Category
        </Button>
      </Menu.Item>
    </Menu>
  );

  const categoryMenu = (
    <Menu>
      {categories.map((category) => (
        <Menu.Item key={category.id}>
          <Space>
            <span>{category.name}</span>
            <Button
              type="link"
              onClick={() => handleCategoryEdit(category)}
              icon={<EditOutlined />}
            />
            <Button
              type="link"
              icon={<DeleteOutlined />}
              onClick={() => handleCategoryDelete(category)}
            />
          </Space>
        </Menu.Item>
      ))}
    </Menu>
  );
  const onDateChange = (dates, dateStrings) => {
    console.log('Selected Dates:', dates);
    console.log('Formatted Dates:', dateStrings);
    // You can add your filter logic here using the selected dates
  };
  const handleGenerateReport = () => {
    // Add your report generation logic here
    console.log('Generating report...');
  };
  const handleViewHide = (record, action) => {
    if (action === 'view') {
      // Set the asset to be viewed
      setViewAsset(record);
      setIsModalVisible(true);
    } else if (action === 'hide') {
      // Implement hide logic here
      setVisibleAssets((prevAssets) =>
        prevAssets.filter((item) => item.key !== record.key)
      );
      notification.info({
        message: 'Asset Hidden',
        description: `Asset "${record.name}" has been hidden.`,
      });
    }
  };
  const handleEditFixedasset = (record) => {
    setEditKey(record.key);
    form.setFieldsValue(record);
    showModal('fixedasset');
  };













  
  return (
    <>
      <Dropdown overlay={menu} placement="bottomLeft">
        <Button  size={size} className='bg-yellow-500 hover:bg-white text-white'>
          Create <DownOutlined />
        </Button>
      </Dropdown>
      <Dropdown overlay={categoryMenu} placement="bottomLeft">
        <Button size={size} style={{ marginLeft: 8 }} >
          Categories <DownOutlined />
        </Button>
      </Dropdown>
      <RangePicker 
        size={size} 
        className="ml-2 border border-gray-300 rounded px-4 py-2 h-10" 
        onChange={onDateChange} 
        placeholder={['Start Date', 'End Date']}
      />
      <Button
        size={size}
        className="absoload ml-96 bg-yellow-500 hover:bg-yellow-50 text-white"
        onClick={handleGenerateReport}
      >
        <DownloadOutlined className="mr-2" /> Generate Report
      </Button>

      <Table columns={columns(handleEdit, handleDelete)} dataSource={data} className='mt-5' />

      <Modal
        title={modalType === 'category' ? 'Category' : 'Fixed Asset'}
        visible={isModalVisible}
        onOk={handleOk}
        onCancel={handleCancel}
      >
       <div style={{ maxHeight: '400px', overflowY: 'auto' }}>
       <Form form={form} layout="vertical">
          {modalType === 'fixedasset' ? (
            <>
              <Form.Item name="name" label="Asset Name" rules={[{ required: true, message: 'Please input the asset name!' }]}>
                <Input />
              </Form.Item>
              <Form.Item name="categoryId" label="Category" rules={[{ required: true, message: 'Please select a category!' }]}>
                <Select>
                  {categories.map((category) => (
                    <Option key={category.id} value={category.id}>
                      {category.name}
                    </Option>
                  ))}
                </Select>
              </Form.Item>
              <Form.Item name="model" label="Model">
                <Input />
              </Form.Item>
              <Form.Item name="year" label="Year">
                <Input />
              </Form.Item>
              <Form.Item name="serialNumber" label="Serial Number">
                <Input />
              </Form.Item>
              <Form.Item name="purchaseDate" label="Purchase Date">
                <DatePicker />
              </Form.Item>
              <Form.Item name="price" label="Price">
                <Input type="number" />
              </Form.Item>
              <Form.Item name="unit" label="Unit">
                <Input />
              </Form.Item>
              <Form.Item name="quantity" label="Quantity">
                <Input type="number" />
              </Form.Item>
            </>
          ) : (
            <>
              <Form.Item
                name="name"
                label="Name"
                rules={[{ required: true, message: 'Please input the name of the category!' }]}
              >
                <Input />
              </Form.Item>
              <Form.Item name="description" label="Description">
                <Input type="textarea" />
              </Form.Item>
            </>
          )}
        </Form>
        </div>
      </Modal>
    </>
  );
};

export default TotalAsset;

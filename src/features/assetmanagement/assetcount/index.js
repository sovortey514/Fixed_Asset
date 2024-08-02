import React, { useState } from 'react';
import { Button, Modal, Form, Input, Select, Dropdown, Menu, notification } from 'antd';
import { DownOutlined } from '@ant-design/icons';

const { Option } = Select;

const TotalAsset = () => {
  const [size, setSize] = useState('large');
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [modalType, setModalType] = useState(''); // 'building', 'department', or 'room'
  const [form] = Form.useForm();
  const [data, setData] = useState([]);

  const showModal = (type) => {
    setModalType(type);
    form.resetFields(); // Reset form fields when opening the modal
    setIsModalVisible(true);
  };

  const handleCancel = () => {
    setIsModalVisible(false);
  };

  const handleOk = async () => {
    try {
      const values = await form.validateFields();
      // Process form values based on modalType
      if (modalType === 'building') {
        console.log('Creating building:', values);
        // Call API to create building
      } else if (modalType === 'department') {
        console.log('Creating department:', values);
        // Call API to create department
      } else if (modalType === 'room') {
        console.log('Creating room:', values);
        // Call API to create room
      }
      notification.success({ message: `${modalType.charAt(0).toUpperCase() + modalType.slice(1)} created successfully!` });
      setIsModalVisible(false);
    } catch (error) {
      console.error('Failed to submit form:', error);
    }
  };

  const createMenu = (
    <Menu>
      <Menu.Item key="1">
        <Button type="link" onClick={() => showModal('building')}>Create Building</Button>
      </Menu.Item>
      <Menu.Item key="2">
        <Button type="link" onClick={() => showModal('department')}>Create Department</Button>
      </Menu.Item>
      <Menu.Item key="3">
        <Button type="link" onClick={() => showModal('room')}>Create Room</Button>
      </Menu.Item>
    </Menu>
  );

  const buildingMenu = (
    <Menu>
      <Menu.Item key="1">
        {/* <Button type="link" onClick={() => showModal('building')}>Create Building</Button> */}
      </Menu.Item>
    </Menu>
  );

  const departmentMenu = (
    <Menu>
      <Menu.Item key="1">
        {/* <Button type="link" onClick={() => showModal('department')}>Create Department</Button> */}
      </Menu.Item>
    </Menu>
  );

  const roomMenu = (
    <Menu>
      <Menu.Item key="1">
        {/* <Button type="link" onClick={() => showModal('room')}>Create Room</Button> */}
      </Menu.Item>
    </Menu>
  );

  return (
    <>
      <Dropdown overlay={createMenu} placement="bottomLeft">
        <Button size={size} className='bg-yellow-500 hover:bg-white text-white'>
          Create <DownOutlined />
        </Button>
      </Dropdown>
      <Dropdown overlay={buildingMenu} placement="bottomLeft">
        <Button size={size} style={{ marginLeft: 8 }}>
          Building <DownOutlined />
        </Button>
      </Dropdown>
      <Dropdown overlay={departmentMenu} placement="bottomLeft">
        <Button size={size} style={{ marginLeft: 8 }}>
          Department <DownOutlined />
        </Button>
      </Dropdown>
      <Dropdown overlay={roomMenu} placement="bottomLeft">
        <Button size={size} style={{ marginLeft: 8 }}>
          Room <DownOutlined />
        </Button>
      </Dropdown>

      <Modal
        title={modalType.charAt(0).toUpperCase() + modalType.slice(1)}
        visible={isModalVisible}
        onOk={handleOk}
        onCancel={handleCancel}
      >
        <Form form={form} layout="vertical">
          {modalType === 'building' && (
            <>
              <Form.Item name="name" label="Building Name" rules={[{ required: true, message: 'Please input the building name!' }]}>
                <Input />
              </Form.Item>
            </>
          )}
          {modalType === 'department' && (
            <>
              <Form.Item name="name" label="Department Name" rules={[{ required: true, message: 'Please input the department name!' }]}>
                <Input />
              </Form.Item>
              <Form.Item name="buildingId" label="Building" rules={[{ required: true, message: 'Please select a building!' }]}>
                <Select>
                  {data.map((building) => (
                    <Option key={building.id} value={building.id}>
                      {building.name}
                    </Option>
                  ))}
                </Select>
              </Form.Item>
              <Form.Item name="floorNumber" label="Floor Number">
                <Input type="number" />
              </Form.Item>
              <Form.Item name="description" label="Description">
                <Input.TextArea />
              </Form.Item>
            </>
          )}
          {modalType === 'room' && (
            <>
              <Form.Item name="name" label="Room Name" rules={[{ required: true, message: 'Please input the room name!' }]}>
                <Input />
              </Form.Item>
              <Form.Item name="departmentId" label="Department" rules={[{ required: true, message: 'Please select a department!' }]}>
                <Select>
                  {data.map((department) => (
                    <Option key={department.id} value={department.id}>
                      {department.name}
                    </Option>
                  ))}
                </Select>
              </Form.Item>
              <Form.Item name="capacity" label="Capacity">
                <Input type="number" />
              </Form.Item>
              <Form.Item name="description" label="Description">
                <Input.TextArea />
              </Form.Item>
            </>
          )}
        </Form>
      </Modal>
    </>
  );
};

export default TotalAsset;

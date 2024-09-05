
import React, { useState } from 'react';

import { Table, Button, Input,Modal ,Space,Tag} from 'antd';
import {
  PlusOutlined
} from "@ant-design/icons";
import { useNavigate } from 'react-router-dom';
import Register from '../user/Register';



const columns = [
  {
    title: "No",
    dataIndex: "key",
    render: (_, r, index) => index + 1,
  },
  {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
    render: (text) => <a>{text}</a>,
  },
  {
    title: 'Email',
    dataIndex: 'email',
    key: 'email',
  },
  {
    title: 'Password',
    dataIndex: 'password',
    key: 'password',
  },
  {
    title: 'Status',
    key: 'status',
    dataIndex: 'status',
    render: (_, { status }) => (
      <>
        {status === 'Active' ? (
          <Tag color="green">
            {status.toUpperCase()}
          </Tag>
        ) : (
          <Tag color="volcano">
            {status.toUpperCase()}
          </Tag>
        )}
      </>
    ),
  },
  {
    title: 'Action',
    key: 'action',
    render: (_, record) => (
      <Space size="middle">
        <a>Invite {record.name}</a>
        <a>Delete</a>
      </Space>
    ),
  },
];
// const data = [
//   {
//     key: '1',
//     name: 'John Brown',
//     age: 32,
//     address: 'New York No. 1 Lake Park',
//     status: 'Active', // Status for John Brown
//   },
//   {
//     key: '2',
//     name: 'Jim Green',
//     age: 42,
//     address: 'London No. 1 Lake Park',
//     status: 'Disable', // Status for Jim Green
//   },
//   {
//     key: '3',
//     name: 'Joe Black',
//     age: 32,
//     address: 'Sydney No. 1 Lake Park',
//     status: 'Active', // Status for Joe Black
//   },
// ];


function ToatalUser() {
  const navigate = useNavigate();
  const handleCreateClick = () => {
    navigate('/register'); 
  };
  const [isModalVisible, setIsModalVisible] = useState(false);

  const showModal = () => {
    setIsModalVisible(true);
  };

  const handleOk = () => {
    setIsModalVisible(false);
  };

  const handleCancel = () => {
    setIsModalVisible(false);
  };
  return (
    <>
     <div className="flex justify-between ">
        <Button
          size="middle"
          style={{ marginLeft: 8 }}
          shape="circle"
          icon={<PlusOutlined />}
          onClick={showModal}
        />
      </div>

      <div className='mt-4'>
        <Table columns={columns} />
      </div>

      <Modal 
        title="Register" 
        visible={isModalVisible} 
        width={800}
        footer={null}
        onCancel={handleCancel}
        
      >
       <Register/>
      </Modal>
    </>
  );
}

export default ToatalUser;

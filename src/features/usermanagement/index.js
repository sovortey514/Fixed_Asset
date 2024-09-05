
import React, { useState, useEffect } from "react";
import { Table, Button, Input,Modal ,Space,Tag} from 'antd';
import {
  PlusOutlined
} from "@ant-design/icons";
import { useNavigate } from 'react-router-dom';
import Register from '../user/Register';
import { showNotification } from "../common/headerSlice";
const token = localStorage.getItem("token");


const columns = [
  {
    title: "No",
    dataIndex: "key",
    render: (_, __, index) => index + 1,
  },
  {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
    render: (text) => <a>{text || 'N/A'}</a>, // Handle null values
  },
  {
    title: 'Email',
    dataIndex: 'email',
    key: 'email',
  },
  {
    title: 'Status',
    key: 'status',
    dataIndex: 'enabled', // Adjust based on the actual data field
    render: (enabled) => (
      <>
        {enabled ? (
          <Tag color="green">
            Active
          </Tag>
        ) : (
          <Tag color="volcano">
            Inactive
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
        <a>Invite {record.name || 'User'}</a>
        <a>Delete</a>
      </Space>
    ),
  },
];


function ToatalUser() {
  const navigate = useNavigate();
  const handleCreateClick = () => {
    navigate('/register'); 
  };
  const [user, setUser] = useState([]);

  useEffect(() => {
    fetchUser();
  }, []);



  const [isModalVisible, setIsModalVisible] = useState(false);

  const showModal = () => {
    setIsModalVisible(true);
  };

  const fetchUser = async () => {
    try {
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };

      const response = await fetch("http://localhost:6060/auth/users", {
        headers,
      });
      const result = await response.json();
      console.log(result);
      if (result) {
        setUser(result || []);
      
      } else {
        showNotification.error({
          message: "Failed to fetch User",
          description: result.error,
        });
      }
    } catch (error) {
      console.error("Error fetching user:", error);
    }
  };
  console.log(user)
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
      <Table columns={columns} dataSource={user.map((u, index) => ({ ...u, key: index }))} rowKey="id" />
    </div>

      <Modal 
        visible={isModalVisible} 
        width={500}
        footer={null}
        onCancel={handleCancel}
      >
       <Register/>
      </Modal>
    </>
  );
}

export default ToatalUser;

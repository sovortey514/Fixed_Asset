
import React, { useState, useEffect } from "react";
import { Table, Button, Input,Modal ,Space,Tag, Tooltip} from 'antd';
import {
  PlusOutlined, EyeOutlined, EditOutlined, DeleteOutlined 
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
    title: 'Username',
    dataIndex: 'username',
    key: 'username',
  },
  {
  title: 'Status',
  key: 'status',
  dataIndex: 'enabled', // Adjust based on the actual data field
  render: (enabled) => (
    <>
      {enabled ? (
        <span
          className="px-2 py-1 rounded-md  bg-green-200 text-green-800"
        >
          Active
        </span>
      ) : (
        <span
          className="px-2 py-1 rounded-md  bg-red-100 text-red-800  "
        >
          Inactive
        </span>
      )}
    </>
  ),
}
,
  {
    title: "Action",
    key: "action",
    render: (_, record) => (
      <Space size="middle">
        {/* Edit Button */}
        <Button
          icon={<EditOutlined />}
          // onClick={() => (handleEdit(record), setItem(record))}
          className="bg-green-600 hover:bg-green-700 text-white border-none rounded-md p-2 shadow-md"
        />
    
        {/* View Button */}
        <Button
          icon={<EyeOutlined />}
          // onClick={() => handleViewHide(record, "view")}
          className="bg-white hover:bg-yellow-500 text-yellow-500 border-none rounded-full p-2 shadow-md"
        />
  
        {/* Delete Button */}
        <Button
          icon={<DeleteOutlined />}
          // onClick={() => handleDelete(record)}
          className="bg-white hover:bg-red-700 text-red-600 border-none rounded-md p-2 shadow-md"
        />
      </Space>
    ),
  }
  
  
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
        const filteredUsers = result.filter(user => user.role !== 'ADMIN');
        setUser(filteredUsers || []);
      
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

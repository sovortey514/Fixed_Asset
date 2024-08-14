import React, { useState, useEffect } from 'react';
import { Button, Modal, Form, Input, Select, Dropdown, Menu, notification, Space } from 'antd';
import { DownOutlined } from '@ant-design/icons';
import { EditOutlined, DeleteOutlined } from '@ant-design/icons';
const { Option } = Select;

const TotalAsset = () => {
  const [size, setSize] = useState('large');
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [modalType, setModalType] = useState(''); // 'building', 'department', or 'room'
  const [form] = Form.useForm();
  const [buildings, setBuildings] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [rooms, setRooms] = useState([]);
  const [assetHolders, setAssetHolder] = useState([]);
  const [selectedBuilding, setSelectedBuilding] = useState('Select Building');
  const [selectedDepartment, setSelectedDepartment] = useState('Select Department');
  const [selectedRoom, setSelectedRoom] = useState('Select Room');
  const [selectedAssetHolder, setSelectedAssetHolder] = useState('Select Asset Holder');

  const [editBuilding, setEditBuilding] = useState(null);
  const [editDepartment, setEditDepartment] = useState(null);
  const [editRoom, setEditRoom] = useState(null);
  const [editAssetHolder, setEditAssetHolder] = useState(null);



  // const [editBuilding, setEditBuilding] = useState(null);
  // const [editBuilding, setEditBuilding] = useState(null);
  const token = localStorage.getItem('token');

  useEffect(() => {
    fetchData();
  }, []);
  
  const fetchData = async () => {
    try {

      const headers = { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` };
      
      let response = await fetch('http://localhost:6060/admin/getAllBuildings', { headers });
      let data = await response.json();
      if (response.ok) setBuildings(data.buildings || []);
      
      response = await fetch('http://localhost:6060/admin/getAllDepartments', { headers });
      data = await response.json();
      if (response.ok) setDepartments(data.departments || []);
      
      response = await fetch('http://localhost:6060/admin/getAllRooms', { headers });
      data = await response.json();
      if (response.ok) setRooms(data.rooms || []);

      response = await fetch('http://localhost:6060/admin/getallassetholders', { headers });
      data = await response.json();
      if (response.ok) setAssetHolder(data.assetHolders || []);
      
    } catch (error) {
      console.error('Failed to fetch data:', error);
    }
  };
  

  const showModal = (type) => {
    setModalType(type);
    form.resetFields(); // Reset form fields when opening the modal
    setIsModalVisible(true);
    setEditBuilding(null);
    setEditDepartment(null)
    setEditRoom(null);
    setEditAssetHolder(null);
  };

  const handleEditBuilding = (building) => {
    setModalType('building');
    setEditBuilding(building);
    form.setFieldsValue({
      name: building.name,
    });
    setIsModalVisible(true);
  };
  const handleEditDepartment = (department) => {
    console.log(department.building.name)
    setModalType('department');
    setEditDepartment(department);
    form.setFieldsValue({
      name: department.name,
      building: department.building.id,
      floorNumber: department.floorNumber,
      description: department.description,
    });
    setIsModalVisible(true);
  };

  const handleEditRoom = (room) => {
    setModalType('room');
    setEditRoom(room);
    form.setFieldsValue({
      name: room.name,
      building: room.building.id, 
      department: room.department.id,
      floorNumber: room.floorNumber,
      description: room.description,
    });
    setIsModalVisible(true);
  };

  const handleEditAssetHolder = (assetholder) => {
    setModalType('assetholder');
    setEditAssetHolder(assetholder);
    form.setFieldsValue({
      name: assetholder.name,
      department: assetholder.department.id, // Assuming department is an object with an id field
      email: assetholder.email, // Example field, adjust based on your form structure
      phoneNumber: assetholder.phoneNumber, // Example field, adjust based on your form structure
    });
    setIsModalVisible(true);
  };
  

  const handleCancel = () => {
    setIsModalVisible(false);
  };

  const handleOk = async () => {
  try {
    const values = await form.validateFields(); // Validate form fields
    const headers = { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` };
    console.log('Form Values:', values);
    let response;
    let data;

    if (modalType === 'building') {
      if (editBuilding) {
        // Edit existing building
        response = await fetch(`http://localhost:6060/admin/updateBuilding/${editBuilding.id}`, {
          method: 'PUT',
          headers,
          body: JSON.stringify(values),
        });

        data = await response.json();
        if (response.ok) {
          setBuildings(prevBuildings =>
            prevBuildings.map(building =>
              building.id === editBuilding.id ? { ...building, ...values } : building
            )
          );
          setSelectedBuilding(data.building.name); // Update selection
          notification.success({
            message: 'Building updated successfully!',
            duration: 1,
          });
          setIsModalVisible(false);
        } else {
          throw new Error(data.error || 'Failed to update building');
        }
      } else {
        // Create new building
        response = await fetch('http://localhost:6060/admin/createBuilding', {
          method: 'POST',
          headers,
          body: JSON.stringify(values),
        });

        data = await response.json();

        if (response.ok) {
          setBuildings([...buildings, data.building]); // Update state
          setSelectedBuilding(data.building.name); // Update selection
          notification.success({
            message: 'Building created successfully!',
            duration: 1,
          });
          setIsModalVisible(false);
        } else {
          throw new Error(data.error || 'Failed to create building');
        }
      }
    } else if (modalType === 'department') {
      if (editDepartment) {
        // Edit existing department
        response = await fetch(`http://localhost:6060/admin/updateDepartment/${editDepartment.id}`, {
          method: 'PUT',
          headers,
          body: JSON.stringify({ ...values, building: { id: values.building } }),
        });
    
        data = await response.json();
        if (response.ok) {
          setDepartments(prevDepartments =>
            prevDepartments.map(department =>
              department.id === editDepartment.id ? { ...department, ...values } : department
            )
          );
          setSelectedDepartment(data.department.name); // Update selection
          notification.success({
            message: 'Department updated successfully!',
            duration: 1,
          });
          setIsModalVisible(false);
        } else {
          throw new Error(data.error || 'Failed to update department');
        }
      } else {
        // Create new department
        response = await fetch('http://localhost:6060/admin/createDepartment', {
          method: 'POST',
          headers,
          body: JSON.stringify({ ...values, building: { id: values.building } }),
        });
        
        data = await response.json();
        
        if (response.ok && data.department) {
          setDepartments(prev => [...prev, data.department]);
          setSelectedDepartment(data.department.name);
          notification.success({
            message: 'Department created successfully!',
            duration: 1,
          });
          setIsModalVisible(false);
        } else {
          throw new Error(data.error || 'Failed to create department');
        }
      }
    }else if (modalType === 'room') {
      if (editRoom) {
        // Edit existing room
        response = await fetch(`http://localhost:6060/admin/updateRoom/${editRoom.id}`, {
          method: 'PUT',
          headers,
          body: JSON.stringify({ ...values, department: { id: values.department }, building: { id: values.building } }),
        });
        
        data = await response.json();
        if (response.ok) {
          setRooms(prevRooms =>
            prevRooms.map(room =>
              room.id === editRoom.id ? { ...room, ...values } : room
            )
          );
          setSelectedRoom(data.room.name); // Update selection
          notification.success({
            message: 'Room updated successfully!',
            duration: 1,
          });
          setIsModalVisible(false);
        } else {
          throw new Error(data.error || 'Failed to update room');
        }
      } else {
        // Create new room
        response = await fetch('http://localhost:6060/admin/createRoom', {
          method: 'POST',
          headers,
          body: JSON.stringify({ ...values, department: { id: values.department }, building: { id: values.building } }),
        });
    
        data = await response.json();
        if (response.ok && data.room) {

          // setRooms([...rooms, data.room]); // Update state
          setSelectedRoom(data.room.name); // Update selection
          notification.success({
            message: 'Room created successfully!',
            duration: 1,
          });
          setIsModalVisible(false);
        } else {
          throw new Error(data.error || 'Failed to create room');
        }
      }
    }
    else if (modalType === 'assetholder') {
      if (editAssetHolder) {
        // Edit existing asset holder
        response = await fetch(`http://localhost:6060/admin/updateassetholderbyId/${editAssetHolder.id}`, {
          method: 'PUT',
          headers,
          body: JSON.stringify({ ...values, department: { id: values.department } }),
        });
    
        data = await response.json();
        if (response.ok) {
          setAssetHolder(prevAssetHolders =>
            prevAssetHolders.map(assetholder =>
              assetholder.id === editAssetHolder.id ? { ...assetholder, ...values } : assetholder
            )
          );
          setSelectedAssetHolder(data.assetholder); // Update selection
          notification.success({
            message: 'Asset holder updated successfully!',
            duration: 1,
          });
          setIsModalVisible(false);
          fetchData();
        } else {
          throw new Error(data.error || 'Failed to update asset holder');
        }
      } else {
        // Create new asset holder
        response = await fetch('http://localhost:6060/admin/createassetholder', {
          method: 'POST',
          headers,
          body: JSON.stringify({ ...values, department: { id: values.department } }),
        });
    
        data = await response.json();
        console.log('Asset Holder Response:', data);
        if (response.ok && data.assetHolder) {
          // setAssetHolder([...assetHolders, data.assetholder]);
          setSelectedAssetHolder(data.assetholder); // Update selection
          notification.success({
            message: 'Asset holder created successfully!',
            duration: 1,
          });
          setIsModalVisible(false);
          fetchData();
        } else {
          throw new Error(data.error || 'Failed to create asset holder');
        }
      }
    }
    }catch (error) {
    console.error('Failed to submit form:', error);
    notification.error({
      message: 'Failed to submit form',
      description: error.message,
      duration: 1,
    });
  }
}; 


  

  const handlebuildingDelete = async (building) => {

    console.log('Building:', building);
    try {
      const token = localStorage.getItem('token');
      const headers = { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` };
      await fetch(`http://localhost:6060/admin/deleteBuilding/${building.id}`, {
        method: 'DELETE',
        headers,
      });
   
      setBuildings((prevBuildings) => prevBuildings.filter((cat) => cat.id !== building.id));

      notification.success({
        message: 'Building  Deleted',
        description: 'Building has been deleted successfully.',
        duration: 1
      });
    } catch (error) {
      console.error('Error deleting building:', error);
      notification.error({
        message: 'Failed to delete building ',
        description: error.message,
        duration: 1
      });
    }
  }

  const handleDepartmentDelete = async (department) => {

    try {
      const token = localStorage.getItem('token');
      const headers = { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` };
      await fetch(`http://localhost:6060/admin/deleteDepartment/${department.id}`, {
        method: 'DELETE',
        headers,
      });
   
      setDepartments((prevDepartments) => prevDepartments.filter((cat) => cat.id !== department.id));

      notification.success({
        message: 'Department Deleted',
        description: 'Department has been deleted successfully.',
        duration: 1
      });
    } catch (error) {
      console.error('Error deleting building:', error);
      notification.error({
        message: 'Failed to delete building ',
        description: error.message,
        duration: 1
      });
    }
  }

  const handleRoomDelete = async (room) => {

    try {
      const token = localStorage.getItem('token');
      const headers = { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` };
      await fetch(`http://localhost:6060/admin/deleteroom/${room.id}`, {
        method: 'DELETE',
        headers,
      });
   
      setRooms((prevRooms) => prevRooms.filter((cat) => cat.id !== room.id));
    } catch (error) {
      console.error('Error deleting room:', error);
      notification.error({
        message: 'Failed to delete room ',
        description: error.message,
        duration: 1
      });
    }
  }

  const handleAssetholderDelete = async (assetHolders) => {
    try {
      const token = localStorage.getItem('token');
      const headers = { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` };
      await fetch(`http://localhost:6060/admin/deleteassetbyId/${assetHolders.id}`, {
        method: 'DELETE',
        headers,
      });
   
      setAssetHolder((prevAssetHolders) => prevAssetHolders.filter((cat) => cat.id !== assetHolders.id));
    } catch (error) {
      console.error('Error deleting assetholder:', error);
      notification.error({
        message: 'Failed to delete assetholder',
        description: error.message,
        duration: 1
      });
    }
  }

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
      <Menu.Item key="4">
        <Button type="link" onClick={() => showModal('assetholder')}>Create Assetholder</Button>
      </Menu.Item>
    </Menu>
  );

  const buildingMenu = (
    <Menu>
    {buildings.map((building) => (
      <Menu.Item key={building.id}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Button type="link" onClick={() => setSelectedBuilding(building.name)}>
            {building.name}
          </Button>
          <div>
            <Button 
              type="link" 
              icon={<EditOutlined />} 
              onClick={() => handleEditBuilding(building)} 
            />
            <Button 
              type="link" 
              icon={<DeleteOutlined />} 
              onClick={() =>  handlebuildingDelete(building)} 
            />
          </div>
        </div>
      </Menu.Item>
    ))}
  </Menu>
  );

  const departmentMenu = (
    <Menu>
    {departments.map((department) => (
      <Menu.Item key={department.id}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Button 
            type="link" 
            onClick={() => setSelectedDepartment(department.name)}
          >
            {department.name}
          </Button>
          <div>
            <Button 
              type="link" 
              icon={<EditOutlined />} 
              onClick={() => handleEditDepartment(department)} 
            />
            <Button 
              type="link" 
              icon={<DeleteOutlined />} 
              onClick={() => handleDepartmentDelete(department)} 
            />
          </div>
        </div>
      </Menu.Item>
    ))}
  </Menu>
  );

  const roomMenu = (
    <Menu>
    {rooms.map((room) => (
      <Menu.Item key={room.id}>
        <div className="flex justify-between items-center w-full">
          <Button
            type="link"
            onClick={() => setSelectedRoom(room.name)}
          >
            {room.name}
          </Button>
          <div className="flex gap-2">
            <Button
              type="link"
              onClick={() => handleEditRoom(room)}
              icon={<EditOutlined />}
            />
            <Button
              type="link"
              icon={<DeleteOutlined />}
              onClick={() => handleRoomDelete(room)}
            />
          </div>
        </div>
      </Menu.Item>
    ))}
  </Menu>
  );
  const assetholderMenu = (
    <Menu>
    {assetHolders.map((assetHolder) => (
      <Menu.Item key={assetHolder.id}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Button 
            type="link" 
            onClick={() => setSelectedAssetHolder(assetHolder)}
          >
            {assetHolder.name}
          </Button>
          <div>
            <Button 
              type="link" 
              icon={<EditOutlined />} 
              onClick={() => handleEditAssetHolder(assetHolder)} 
            />
            <Button 
              type="link" 
              icon={<DeleteOutlined />} 
              onClick={() => handleAssetholderDelete(assetHolder)} 
            />
          </div>
        </div>
      </Menu.Item>
    ))}
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
      <Dropdown overlay={assetholderMenu} placement="bottomLeft">
        <Button size={size} style={{ marginLeft: 8 }}>
          AssetHolder <DownOutlined />
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
              <Form.Item name="building" label="Building" rules={[{ required: true, message: 'Please select a building!' }]}>
                <Select>
                  {buildings.map((building) => (
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
              <Form.Item name="building" label="Building" rules={[{ required: true, message: 'Please select a building!' }]}>
                <Select>
                  {buildings.map((building) => (
                    <Option key={building.id} value={building.id}>
                      {building.name}
                    </Option>
                  ))}
                </Select>
              </Form.Item>
              <Form.Item name="department" label="Department" rules={[{ required: true, message: 'Please select a department!' }]}>
                <Select>
                  {departments.map((department) => (
                    <Option key={department.id} value={department.id}>
                      {department.name}
                    </Option>
                  ))}
                </Select>
              </Form.Item>
              
              <Form.Item name="description" label="Description">
                <Input.TextArea />
              </Form.Item>
            </>
          )} 
          {modalType === 'assetholder' && (
          <>
            <Form.Item 
              name="name" 
              label="Asset Holder Name" 
              rules={[{ required: true, message: 'Please input the asset holder\'s name!' }]}
            >
              <Input />
            </Form.Item>
            <Form.Item 
              name="email" 
              label="Email" 
              rules={[{ required: true, type: 'email', message: 'Please input a valid email!' }]}
            >
              <Input />
            </Form.Item>
            <Form.Item 
              name="phoneNumber" 
              label="Phone Number" 
              rules={[{ required: true, message: 'Please input the phone number!' }]}
            >
              <Input />
            </Form.Item>
            <Form.Item 
              name="department" 
              label="Department" 
              rules={[{ required: true, message: 'Please select a department!' }]}
            >
              <Select>
                {departments.map((department) => (
                  <Option key={department.id} value={department.id}>
                    {department.name}
                  </Option>
                ))}
              </Select>
            </Form.Item>
          </>
    )}
        </Form>
      </Modal>
    </>
  );
};

export default TotalAsset;

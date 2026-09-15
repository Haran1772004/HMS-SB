package com.hospital.hospital_spring.model;


public class PatientRegistrationRequest {

    
    private String username;
    private String password;
    private String name;
    private String dob;         
    private String gender;      
    private String phone;
    private String email;
    private String state;
    private String district;
    private String pincode;
    private String addressType;

    public PatientRegistrationRequest() {
    }

    public String getUsername() { 
        return username;
    }
    public void setUsername(String username) { 
        this.username = username; 
    }

    public String getPassword() { 
        return password; 
    }

    public void setPassword(String password) { 
        this.password = password; 
    }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public String getDob() { 
        return dob; 
    }
    public void setDob(String dob) { 
        this.dob = dob; 
    }

    public String getGender() { 
        return gender;
    }

    public void setGender(String gender) { 
        this.gender = gender; 
    }

    public String getPhone() { 
        return phone; 
    }

    public void setPhone(String phone) { 
        this.phone = phone; 
    }

    public String getEmail() { 
        return email; 
    }

    public void setEmail(String email) { 
        this.email = email;
    }

    public String getState() { 
        return state;
    }

    public void setState(String state) { 
        this.state = state; 
    }

    public String getDistrict() { 
        return district; 
    }

    public void setDistrict(String district) { 
        this.district = district;
    }

    public String getPincode() { 
        return pincode;
    }

    public void setPincode(String pincode) { 
        this.pincode = pincode;
    }

    public String getAddressType() { 
        return addressType; 
    }

    public void setAddressType(String addressType) { 
        this.addressType = addressType; 
    }
}

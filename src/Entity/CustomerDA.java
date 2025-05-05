package Entity;
import java.sql.*;
import java.util.*;
import javax.persistence.*;
import javax.transaction.UserTransaction;

public class CustomerDA {

    private EntityManager em;
    private UserTransaction utx;
    public CustomerDA() {
    }

    public CustomerDA(EntityManager em,UserTransaction utx) {
        this.em = em;
        this.utx = utx;
    }

    public boolean compareCustomerEmail(String email) {
        try {
            Query query = em.createNamedQuery("Customer.findByEmail", Customer.class);
            query.setParameter("email", email);
            Customer customer =(Customer)query.getSingleResult();
            return customer != null;
        } catch (NoResultException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean isValidEmail(String email) {
    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    return email.matches(emailRegex);
}
    public boolean compareCustomerIC(String ic) {
        try {
            ic = ic.replace("-", "");
            Query query = em.createNamedQuery("Customer.findByIc", Customer.class);
            query.setParameter("ic", ic);
            Customer customer = (Customer)query.getSingleResult();
            return customer != null;
        } catch (NoResultException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean compareCustomerIC(String ic, String email) throws Exception {
    try {
        ic = ic.replace("-", "");
        TypedQuery<Customer> query = em.createQuery(
            "SELECT c FROM CUSTOMER c WHERE c.IC = :IC AND c.EMAIL <> :EMAIL", Customer.class
        );
        query.setParameter("IC", ic);
        query.setParameter("EMAIL", email);
        
        Customer customer = query.getSingleResult();
        return customer != null;
    } catch (NoResultException e) {
        return false;
    } catch (Exception e) {
        throw new Exception("Error while checking IC: " + e.getMessage());
    }
}
    public boolean ifCustomerICOwner(String ic,String email) {
        try {
            ic = ic.replace("-", "");
            Query query = em.createNamedQuery("Customer.findByIc", Customer.class);
            query.setParameter("EMAIL", email);
            Customer customer = (Customer)query.getSingleResult();
                System.out.print(ic);
                System.out.print(customer.getIc());
            return customer.getIc().equals(ic);
        } catch (NoResultException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public Customer getCustomerByEmail(String email) throws Exception {
        try {
            Query query = em.createNamedQuery("Customer.findByEmail", Customer.class);
            query.setParameter("EMAIL", email);
            System.out.print("Make sure:"+(Customer) query.getSingleResult());
            return (Customer) query.getSingleResult(); 
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new Exception("Error retrieving customer by email: " + e.getMessage());
        }
    }
    public boolean compareCustomerPhone(String phone) throws Exception {
        try {
            Query query = em.createNamedQuery("Customer.findByPhoneno",Customer.class);
            query.setParameter("PHONE", phone);
            Customer existingCustomer = (Customer) query.getSingleResult();
            
            return existingCustomer != null;
        } catch (NoResultException e) {
            return false;
        } catch (Exception e) {
            throw new Exception("Error while checking phone number: " + e.getMessage());
        }
    }
    public boolean isPhoneUsedByOtherCustomer(String phone, String currentEmail) throws Exception {
    try {
        Query query = em.createQuery(
            "SELECT c FROM CUSTOMER c WHERE c.PHONENO = :PHONE AND c.EMAIL <> :EMAIL", Customer.class);
        query.setParameter("PHONE", phone);
        query.setParameter("EMAIL", currentEmail);
        List<Customer> result = query.getResultList();
        return !result.isEmpty();
    } catch (Exception e) {
        throw new Exception("Error while checking phone number: " + e.getMessage());
    }
}


    public int getAvailableCustomerId() {
        try {
            TypedQuery<String> query = em.createNamedQuery("Customer.findAllCustomerIds", String.class);
            List<String> Ids = query.getResultList();
            int expected = 1; //start check from c00001
            for (String id : Ids) {
                if (id != null && id.length() > 1) {
                    int num = Integer.parseInt(id.substring(1)); 
                    if (num != expected) {
                        return expected; 
                    }
                    expected++;
                    
                }
            }
            return expected;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        
    }

    // Check if login is valid
    public boolean isLoginValid(String email, String password) {
        try {
            Query query = em.createNamedQuery("Customer.findByEmail", Customer.class);
            query.setParameter("EMAIL", email);
            Customer customer =(Customer) query.getSingleResult();
            if (customer != null) {
                System.out.print("Entered password:"+PasswordUtils.hashPassword(password).substring(0, Math.min(PasswordUtils.hashPassword(password).length(), 20)));
                System.out.print("Database password:"+customer.getPassword());
                return PasswordUtils.hashPassword(password).substring(0, Math.min(PasswordUtils.hashPassword(password).length(), 20)).equals(customer.getPassword());
            }
        } catch (NoResultException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    
    public void SignUpCustomer(String email,String password){
        try {
                int availableCustomerId = getAvailableCustomerId();
                String hashedPassword = PasswordUtils.hashPassword(password);
                hashedPassword = hashedPassword.substring(0, Math.min(hashedPassword.length(), 20));
                String customerId = "c" + String.format("%05d", availableCustomerId);
                System.out.print(availableCustomerId);
                Customer customerInsert = new Customer(customerId, "", "", "", "", email, hashedPassword,"");

                utx.begin();
                em.persist(customerInsert);
                utx.commit();
                } catch (Exception e) {
                try {
                    if (utx != null && utx.getStatus() == javax.transaction.Status.STATUS_ACTIVE) {
                        utx.rollback();
                    }
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
    
    }
    public void ContDetailSignUp(String email,String customerName,String phone,String ic,String address){
        try {
                utx.begin();
                Query query = em.createNamedQuery("Customer.findByEmail", Customer.class);
                query.setParameter("EMAIL", email);

                Customer existingCustomer = (Customer)query.getSingleResult();

                if (existingCustomer != null) {
                    existingCustomer.setCustomername(customerName);
                    existingCustomer.setIc(ic.replace("-", ""));
                    existingCustomer.setPhoneno(phone);
                    existingCustomer.setHomeaddress(address);
                    em.merge(existingCustomer);
                    utx.commit();
                    
                }
            }catch (Exception e) {
                try {
                    if (utx != null && utx.getStatus() == javax.transaction.Status.STATUS_ACTIVE) {
                        utx.rollback();
                    }
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
        
    }
    
    
    
}

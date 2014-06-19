package jpt.test;

public class CustomerList
{
	private static final Customer[] _customers = (CustomerDao.getInstance().findAll()).toArray(new Customer[50]);

	public Customer[] getCustomers()
	{
		return _customers;
	}

	public void deleteCustomer(Integer id)
	{
		CustomerDao dao = CustomerDao.getInstance();
		dao.delete(dao.findById(id));
	}

	public void foo(Integer id)
	{
		CustomerDao dao = CustomerDao.getInstance();
		dao.delete(dao.findById(id));
	}
}
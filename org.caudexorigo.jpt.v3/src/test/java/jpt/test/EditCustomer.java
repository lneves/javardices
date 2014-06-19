package jpt.test;

/**
 * Edit customer bench page for JPT.
 * 
 * @author Luis Neves
 */
public class EditCustomer
{
	private Customer _customer;
	private String _action;
	private static final String EDIT = "edit";
	private static final String SAVE = "save";
	private Integer _id;

	public EditCustomer()
	{
		if (_action.equals(EDIT))
		{
			_customer = CustomerDao.getInstance().findById(_id);
		}
		else if (_action.equals(SAVE))
		{
			CustomerDao.getInstance().saveOrUpdate(_customer);
		}
	}

	public Customer getCustomer()
	{
		return _customer == null ? new Customer() : _customer;
	}

	public String[] getStates()
	{
		return CustomerDao.STATES;
	}

	public void setAction(String action)
	{
		_action = action;
	}

	public void setId(Integer id)
	{
		_id = id;
	}
}
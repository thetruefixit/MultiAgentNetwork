/**
 * filename: Employee.java
 *
 * This demonstrates how to derive a user-defined type
 * based on ORAData and ORADataFactory.
 * This class is used by ORADataExample.java
 *
 * Please use jdk1.2 or later version, classes12.zip
 **/

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Connection;
import oracle.jdbc.OracleConnection;
import oracle.sql.*;
import java.sql.*;
public class Employee implements ORAData, ORADataFactory
{

  static final Employee _employeeFactory = new Employee(null, null);

  public static ORADataFactory getFactory()
  {
    return _employeeFactory;
  }

  public Employee ()
  {
  }

  /* constructor */
  public Employee(String empName, BigDecimal empNo)
  {
    this.empName = empName;
    this.empNo = empNo;
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    Object [] attributes = { empName, empNo };
    Struct s = c.createStruct( "HR.EMPLOYEE", attributes);
    return (Datum)s;
  }

  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null;

    Object [] attributes = ((Struct) d).getAttributes();

    return new Employee((String) attributes[0],
                        (BigDecimal) attributes[1]);
  }

  /* fields */
  public String empName;
  public BigDecimal empNo;
}



package wstest.srv;

import wstest.srv.actors.COfTestStruct;
import wstest.srv.actors.Order;
import wstest.srv.actors.Synthetic;
import wstest.srv.actors.TestNode;

public interface IWSTest {
  void echoVoid();

  COfTestStruct echoStruct(COfTestStruct array);

  TestNode echoList(TestNode list);

  Synthetic echoSynthetic(Synthetic synth);

  Order getOrder(int orderId, int customerId, int messageSize);
}

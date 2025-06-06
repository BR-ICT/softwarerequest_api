import { combineReducers } from "redux";
import companyReducer from "./company.reducer";
import periodReducer from "./period.reducer";
import warehouseReducer from "./warehouse.reducer";
import buReducer from "./bu.reducer";
import departmentReducer from "./department.reducer";
import costcenterReducer from "./costcenter.reducer";
import approveReducer from "./approve.reducer";
import buyerReducer from "./buyer.reducer";
import itemReducer from "./item.reducer";
import itemdetailReducer from "./itemdetail.reducer";
import itemprdetailReducer from "./itemprdetail.reducer";
import loginReducer from "./login.reducer";
import prnumberReducer from "./prnumber.reducer";
import prnumberbuyerReducer from "./prnumberbuyer.reducer";
import prheadReducer from "./prhead.reducer";
import prheadapproveReducer from "./prheadapprove.reducer";
import prdetailReducer from "./prdetail.reducer";
import prdetailbuyerReducer from "./prdetailbuyer.reducer";
import itemunitReducer from "./itemunit.reducer";
import phgroupReducer from "./phgroup.reducer";
import phbuyerReducer from "./phbuyer.reducer";
import supplierReducer from "./supplier.reducer";
import prconfirmbuyerReducer from "./prconfirmbuyer.reducer";
import monthReducer from "./month.reducer";
import statusReducer from "./status.reducer";
import sendemailReducer from "./sendemail.reducer";
import genpoReducer from "./genpo.reducer";
import deptandcostReducer from "./deptandcost.reducer";
import capexReducer from "./capex.reducer";
import deliveryReducer from "./delivery.reducer";
import paymentReducer from "./payment.reducer";
import textm3Reducer from "./textm3.reducer";
import chargeReducer from "./charge.reducer";
import expenitureReducer from "./expeniture.reducer";
import adrnumberReducer from "./adrnumber.reducer";
import adrheadReducer from "./adrhead.reducer";
import adrdetailReducer from "./adrdetail.reducer";
import accountantReducer from "./accountant.reducer";
import marheadReducer from "./marhead.reducer";
import mardetailReducer from "./mardetail.reducer";
import marnumberReducer from "./marnumber.reducer";
import marfileReducer from "./marfile.reducer";
import monitoringreceiptReducer from "./monitoringreceipt.reducer";
import swrfileReducer from "./swrfile.reducer";
import swrfilenumberReducer from "./swrfilenumber.reducer";
import swrheaderReducer from "./swrheader.reducer";
import swrIDReducer from "./swrID.reducer";
// import swrdevReducer from "./swrdev.reducer";
import employeeReducer from "./employee.reducer";
import followerReducer from "./follower.reducer";
import orderidReducer from "./orderid.reducer";
import operationdataReducer from "./operatordata.reducer";
import createIDReducer from "./createid.reducer";
import showvisitorReducer from "./showvisitor.reducer";
import getimageReducer from "./getimage.reducer";
import locationReducer from "./location.reducer";
import scheduleReducer from "./schedule.reducer";
import getpayerReducer from "./getpayer.reducer";
import getinvoiceidReducer from "./getinvoiceid.reducer";

export default combineReducers({
  companyReducer,
  periodReducer,
  warehouseReducer,
  buReducer,
  departmentReducer,
  costcenterReducer,
  approveReducer,
  buyerReducer,
  itemReducer,
  itemdetailReducer,
  itemprdetailReducer,
  itemunitReducer,
  loginReducer,
  prnumberReducer,
  prnumberbuyerReducer,
  prheadReducer,
  prheadapproveReducer,
  prdetailReducer,
  prdetailbuyerReducer,
  phgroupReducer,
  phbuyerReducer,
  supplierReducer,
  prconfirmbuyerReducer,
  monthReducer,
  statusReducer,
  sendemailReducer,
  genpoReducer,
  deptandcostReducer,
  capexReducer,
  deliveryReducer,
  paymentReducer,
  textm3Reducer,
  chargeReducer,
  expenitureReducer,
  adrnumberReducer,
  adrheadReducer,
  adrdetailReducer,
  accountantReducer,
  marheadReducer,
  mardetailReducer,
  marnumberReducer,
  marfileReducer,
  monitoringreceiptReducer,
  swrfileReducer,
  swrfilenumberReducer,
  swrheaderReducer,
  swrIDReducer,
  // swrdevReducer,
  employeeReducer,
  followerReducer,
  orderidReducer,
  operationdataReducer,
  showvisitorReducer,
  getimageReducer,
  locationReducer,
  scheduleReducer,
  createIDReducer,
  getpayerReducer,
  getinvoiceidReducer,
});

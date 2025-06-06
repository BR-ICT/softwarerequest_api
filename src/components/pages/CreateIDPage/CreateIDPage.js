import React, { useRef, useState, useEffect } from "react";
import { Paper, Grid } from "@material-ui/core";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TableContainer,
  Collapse,
  Paper1,
} from "@mui/material";
import DeleteForeverIcon from "@mui/icons-material/DeleteForever";
import {
  Button,
  MenuItem,
  FormControl,
  Select,
  InputLabel,
  Box,
  TextField,
  FormControlLabel,
  Checkbox,
} from "@material-ui/core";
import MaterialTable from "material-table";
import { Autocomplete } from "@mui/material"; // เพิ่มการ import Autocomplete
import * as GetpayerActions from "../../../actions/getpayer.action";
import * as GetinvoiceidActions from "../../../actions/getinvoiceid.action";

import * as CreateIDActions from "../../../actions/createID.action";
import * as SplitActions from "../../../actions/splitBM.action";

import { useSelector, useDispatch } from "react-redux";
import {
  red,
  green,
  purple,
  teal,
  deepOrange,
  blueGrey,
  yellow,
} from "@material-ui/core/colors/";
import {
  makeStyles,
  withStyles,
  ThemeProvider,
  createMuiTheme,
  createTheme,
} from "@material-ui/core/styles";
import { JsonWebTokenError } from "jsonwebtoken";

export default function UploadWithDropdown() {
  const dispatch = useDispatch();

  const useStyles = makeStyles((theme) => ({
    root: {
      width: "100%",
      marginTop: 60,
    },
    paper: {
      padding: theme.spacing(2),
      color: "red",
      backgroundColor: "#efe5d1",
      backgroundSize: "cover",
      backgroundPosition: "center",
      width: "100%", // ตั้งค่าความกว้างให้เท่ากับ MaterialTable
      marginBottom: theme.spacing(2), // เพิ่มระยะห่างระหว่าง Paper กับ MaterialTable
    },
    margin: {
      marginTop: "0.4rem",
      marginRight: "0.4rem",
      margin: theme.spacing(0.3),
    },
    extendedIcon: {
      marginRight: theme.spacing(1),
    },
    row: {
      borderLeft: 1,
      borderRight: 1,
      borderBottom: 1,
      borderTop: 1,
      borderColor: "#E0E0E0",
      borderStyle: "solid",
    },
    wrapper: {
      margin: "3px",
      position: "relative",
    },
    buttonSuccess: {
      backgroundColor: green[500],
      "&:hover": {
        backgroundColor: green[700],
      },
    },
    fabProgress: {
      color: green[500],
      position: "absolute",
      top: -6,
      left: -6,
      zIndex: 1,
    },
    buttonProgress: {
      color: green[500],
      position: "absolute",
      top: "50%",
      left: "50%",
      marginTop: -12,
      marginLeft: -12,
    },
  }));
  const classes = useStyles();

  const [isSplit, setIsSpit] = useState("99"); // สำหรับเก็บค่าจาก Dropdown

  const [selectedValue, setSelectedValue] = useState(""); // สำหรับเก็บค่าจาก Dropdown
  const [tableData, setTableData] = useState([]); // ข้อมูลสำหรับ MaterialTable
  const [columns, setColumns] = useState([]); // คอลัมน์สำหรับ MaterialTable
  const [editRow, setEditRow] = useState(null); // สถานะสำหรับแถวที่ถูกแก้ไข

  const [visibleEdit, setvisibleEdit] = useState(false);

  const createIDReducer = useSelector(({ createIDReducer }) => createIDReducer);
  const getpayerReducer = useSelector(({ getpayerReducer }) => getpayerReducer);
  const getinvoiceidReducer = useSelector(
    ({ getinvoiceidReducer }) => getinvoiceidReducer
  );

  const [selectedValueType, setSelectedValueType] = useState("TRANSFER"); // กำหนดค่าเริ่มต้น

  const [selectedValueLocation, setSelectedValueLocation] = useState("LS"); // กำหนดค่าเริ่มต้น

  const menuItemsType = [
    { VAL: "TRANSFER", ID: "TRANSFER" },
    { VAL: "TRANSFER_EXP", ID: "TRANSFER_EXP" },
    { VAL: "TRANSFER_DEPOSIT", ID: "TRANSFER_DEPOSIT" },
  ];

  const menuItemsLocation = [
    { VAL: "LS", ID: "LS" },
    { VAL: "DC_CM", ID: "DC_CM" },
    { VAL: "HC_PCB", ID: "HC_PCB" },
    { VAL: "ACCOUNT", ID: "ACCOUNT" },
    { VAL: "FM", ID: "FM" },
    { VAL: "OTHER_FM", ID: "OTHER_FM" },
    { VAL: "OTHER", ID: "OTHER" },
  ];

  const hiddenColumnsSCB = [
    "PAYER",
    "OVERPAY",
    "CNDN",
    "BANKCHARGE",
    "GROUPID",
    "H_RNNO",
    "BM_PARENT_STS",
    "BM_PARENT_ID",
    "ID",
    "PAYERNAME",
    "BM_CONO",
    "H_CONO",
    "OKCONO",
    "OKCUNO",
    "OKCUNM",
    "TYPE",
    "LOCATION",
    "00017",
    "LOCATIONTH",
    "HCTYPE",
    "HC_FNNO",
    "HR_CONO",
  ]; // กำหนดชื่อคอลัมน์ที่ไม่ต้องการแสดง

  useEffect(() => {
    dispatch(GetpayerActions.getpayer());
  }, []);

  useEffect(() => {
    dispatch(GetinvoiceidActions.getinvoiceid());
  }, []);

  useEffect(() => {
    if (createIDReducer.result) {
      //alert(JSON.stringify(createIDReducer.result[0]));//
      const generatedColumns = Object.keys(createIDReducer.result[0] || {})
        .filter((key) => !hiddenColumnsSCB.includes(key)) // กรองคีย์ที่ไม่ต้องการออก
        .map((key) => ({
          title: key,
          field: key,
          //editable: "never",
          editable: key === "BM_AMOUNT" ? "always" : "never", // ตรวจสอบว่าเป็น BM_AMOUNT หรือไม่
        }));

      // เพิ่มคอลัมน์ PAYER และ PAYERNAME โดยใช้ editComponent
      // const payerColumn = {
      //   title: "PAYER",
      //   field: "PAYER",
      //   editable: "always",
      //   cellStyle: { width: "300px" },
      //   headerStyle: { width: "300px" },
      //   editComponent: ({ value, onChange, rowData }) => (
      //     <Autocomplete
      //       options={getpayerReducer.result || []}
      //       getOptionLabel={(option) => option.OKSHOW || ""}
      //       value={
      //         getpayerReducer.result.find(
      //           (option) => option.OKCUNO === value
      //         ) || null
      //       }
      //       onChange={(event, newValue) => {
      //         // อัปเดต PAYER และ PAYERNAME เมื่อเลือกใน Autocomplete

      //         onChange(newValue?.OKCUNO || "");

      //         const updatedData = [...tableData];
      //         const rowIndex = updatedData.findIndex(
      //           (row) => row.tableData.id === rowData.tableData.id
      //         );

      //         if (rowIndex >= 0) {
      //           updatedData[rowIndex] = {
      //             ...updatedData[rowIndex],
      //             PAYER: newValue?.OKCUNO || "", // อัปเดต PAYER
      //             VOUCHER: newValue?.OKCUNO || "", // อัปเดต PAYERNAME
      //           };
      //           setTableData(updatedData); // อัปเดต tableData
      //         }
      //       }}
      //       renderInput={(params) => (
      //         <TextField {...params} variant="outlined" size="small" />
      //       )}
      //     />
      //   ),
      // };

      /////

      // const payerColumn = {
      //   title: "PAYER",
      //   field: "PAYER",
      //   // editable: "never",
      //   headerStyle: {
      //     maxWidth: 50,
      //     whiteSpace: "nowrap",
      //     textAlign: "center",
      //   },
      //   cellStyle: {
      //     textAlign: "center",
      //     borderLeft: 1,
      //     borderRight: 1,
      //     borderBottom: 1,
      //     borderTop: 1,
      //     borderColor: "#E0E0E0",
      //     borderStyle: "solid",
      //     paddingLeft: "6px",
      //     paddingRight: "6px",
      //     paddingBottom: "12px",
      //     paddingTop: "12px",
      //   },
      //   editComponent: (props, item) => {
      //     console.log(JSON.stringify(getpayerReducer.result));
      //     return (
      //       <TextField
      //         //disabled={props.value === "TRANSFER" ? true : false}
      //         fullWidth
      //         select
      //         value={props.value}
      //         onChange={(event) => props.onChange(event.target.value)}
      //         SelectProps={{
      //           native: true,
      //         }}
      //       >
      //         {handleEval(getpayerReducer.result).map((option) => (
      //           <option key={option.OKCUNO} value={option.OKCUNO}>
      //             {option.OKSHOW}
      //           </option>
      //         ))}
      //       </TextField>
      //     );
      //   },
      // };

      const payerColumn = {
        title: "PAYER",
        field: "PAYER",
        headerStyle: {
          whiteSpace: "nowrap",
          textAlign: "center",
        },
        cellStyle: {
          textAlign: "center",
          borderLeft: 1,
          borderRight: 1,
          borderBottom: 1,
          borderTop: 1,
          borderColor: "#E0E0E0",
          borderStyle: "solid",
          paddingLeft: "6px",
          paddingRight: "6px",
          paddingBottom: "12px",
          paddingTop: "12px",
        },
        editComponent: (props) => {
          const options = handleEval(getpayerReducer.result);

          return (
            <Autocomplete
              disabled={
                (props.rowData.BM_PARENT_STS === "77" &&
                  props.rowData.GROUPID === "-") ||
                (props.rowData.BM_PARENT_STS === "88" &&
                  props.rowData.GROUPID === "-")
                  ? true
                  : false
              }
              fullWidth
              options={options}
              getOptionLabel={(option) => option.OKSHOW}
              value={
                options.find((option) => option.OKCUNO === props.value) || null
              }
              onChange={(event, newValue) => {
                props.onChange(newValue ? newValue.OKCUNO : "");
              }}
              renderInput={(params) => (
                <TextField
                  disabled={
                    props.rowData.BM_PARENT_STS === "77" &&
                    props.rowData.GROUPID === "-"
                      ? true
                      : false
                  }
                  {...params}
                  label="Select Payer"
                  variant="outlined"
                  style={{ width: "400px" }}
                />
              )}
            />
          );
        },
      };

      const handleEditSplit = (value, id) => {
        console.log("parent " + value);

        if (id !== "-") {
          return true;
        }
        if (value === "88") {
          // Parent
          return true;
        } else if (value === "77") {
          // Child
          return true;
        } else {
          // Normal 00
          return false;
        }
      };

      //////

      const payerNameColumn = {
        title: "PAYERNAME",
        field: "PAYERNAME",
        editable: "never",
        // editable: (rowData) => {
        //   // ตรวจสอบค่าในคอลัมน์ GROUPID ว่าเป็น '-' หรือไม่
        //   return rowData.GROUPID !== '-' ? "never" : "always";
        // },
      };

      const splitbuttonColumn = {};

      const bankChargeColumn = {
        title: "BANKCHARGE",
        field: "BANKCHARGE",
        editable: "always", // ไม่สามารถแก้ไขได้โดยตรง
        default: "0.00", // ค่าเริ่มต้น (เช่น "ค่าเริ่มต้นที่ต้องการ")
        editComponent: (props, item) => {
          return (
            <TextField
              fullWidth
              // disabled={
              //   (props.rowData.BM_PARENT_STS === "00" &&
              //     props.rowData.GROUPID === "-") ||
              //   props.rowData.PAYER === "-"
              //     ? true
              //     : true
              // }
              value={props.value || "0.00"}
              onChange={(event) => props.onChange(event.target.value)}
              SelectProps={{
                native: true,
              }}
            ></TextField>
          );
        },
      };

      const overPayColumn = {
        title: "OVERPAY",
        field: "OVERPAY",
        editable: "always", // ไม่สามารถแก้ไขได้โดยตรง
        default: "0.00", // ค่าเริ่มต้น (เช่น "ค่าเริ่มต้นที่ต้องการ")
        editComponent: (props, item) => {
          return (
            <TextField
              fullWidth
              // disabled={
              //   (props.rowData.BM_PARENT_STS === "00" &&
              //     props.rowData.GROUPID === "-") ||
              //   props.rowData.PAYER === "-"
              //     ? true
              //     : true
              // }
              value={props.value || "0.00"}
              onChange={(event) => props.onChange(event.target.value)}
              SelectProps={{
                native: true,
              }}
            ></TextField>
          );
        },
      };

      const CNDNColumn = {
        title: "CNDN",
        field: "CNDN",
        editable: "always", // ไม่สามารถแก้ไขได้โดยตรง
        default: "0.00", // ค่าเริ่มต้น (เช่น "ค่าเริ่มต้นที่ต้องการ")
        editComponent: (props, item) => {
          return (
            <TextField
              fullWidth
              // disabled={
              //   (props.rowData.BM_PARENT_STS === "00" &&
              //     props.rowData.GROUPID === "-") ||
              //   props.rowData.PAYER === "-"
              //     ? true
              //     : true
              // }
              value={props.value || "0.00"}
              onChange={(event) => props.onChange(event.target.value)}
              SelectProps={{
                native: true,
              }}
            ></TextField>
          );
        },
      };

      const typeColumn = {
        title: "TYPE",
        field: "TYPE",
        // editable: "never",
        headerStyle: {
          maxWidth: 50,
          whiteSpace: "nowrap",
          textAlign: "center",
        },
        cellStyle: {
          textAlign: "center",
          borderLeft: 1,
          borderRight: 1,
          borderBottom: 1,
          borderTop: 1,
          borderColor: "#E0E0E0",
          borderStyle: "solid",
          paddingLeft: "6px",
          paddingRight: "6px",
          paddingBottom: "12px",
          paddingTop: "12px",
        },
        editComponent: (props, item) => {
          return (
            <TextField
              //disabled={props.value === "TRANSFER" ? true : false}
              // disabled={
              //   (props.rowData.BM_PARENT_STS === "00" &&
              //     props.rowData.GROUPID === "-") ||
              //   props.rowData.PAYER === "-"
              //     ? true
              //     : true
              // }
              fullWidth
              select
              value={props.value || "TRANSFER"}
              onChange={(event) => props.onChange(event.target.value)}
              SelectProps={{
                native: true,
              }}
            >
              {handleEval(menuItemsType).map((option) => (
                <option key={option.ID} value={option.VAL}>
                  {option.VAL}
                </option>
              ))}
            </TextField>
          );
        },
      };

      const isSplitColumn = {
        title: "ISSPLIT",
        field: "BM_PARENT_STS",
        // editable: "never",
        headerStyle: {
          maxWidth: 50,
          whiteSpace: "nowrap",
          textAlign: "center",
        },
        cellStyle: {
          textAlign: "center",
          borderLeft: 1,
          borderRight: 1,
          borderBottom: 1,
          borderTop: 1,
          borderColor: "#E0E0E0",
          borderStyle: "solid",
          paddingLeft: "6px",
          paddingRight: "6px",
          paddingBottom: "12px",
          paddingTop: "12px",
        },

        editComponent: (props, item) => {
          console.log(JSON.stringify(props));

          // alert(props.rowData.BM_PARENT_STS);
          if (props.value === "77") {
            return null;
          } else {
            return (
              <FormControlLabel
                control={
                  <Checkbox
                    disabled={handleEditSplit(
                      props.rowData.BM_PARENT_STS,
                      props.rowData.GROUPID
                    )}
                    checked={props.rowData.BM_PARENT_STS === "88"} // หรือกำหนดเงื่อนไขตามต้องการ
                    onChange={(event) => {
                      props.onChange(event.target.checked ? "88" : "99");
                      //setIsSpit(event.target.checked); // กำหนดให้ setIsSplit("true") เมื่อมีการเปลี่ยนแปลง
                      //alert(isSplit);
                    }}
                  />
                }
                label="SPLIT" // กำหนดชื่อของ checkbox
              />
            );
          }
        },
      };

      const handleEval = (text) => {
        const evalData = eval(text);
        // console.log(evalData);
        return evalData;
      };

      // const typeColumn = {
      //   title: "TYPE",
      //   field: "TYPE",
      //   editable: "always", // ให้สามารถแก้ไขได้
      //   editComponent: ({ value, onChange }) => (
      //     <FormControl variant="outlined" size="small" fullWidth>
      //       <Select
      //         value={selectedValueType} // ตรวจสอบค่าปัจจุบัน
      //         onChange={(event) => {
      //           const newValue = event.target.value;
      //           setSelectedValueType(newValue); // อัปเดต state
      //           //onChange(newValue); // อัปเดตข้อมูลใน MaterialTable
      //         }}
      //       >
      //         <MenuItem value="TRANSFER">TRANSFER</MenuItem>
      //         <MenuItem value="TRANSFER_EXP">TRANSFER_EXP</MenuItem>
      //         <MenuItem value="TRANSFER_DEPOSIT">TRANSFER_DEPOSIT</MenuItem>
      //       </Select>
      //     </FormControl>
      //   ),
      // };

      // const locationColumn = {
      //   title: "LOCATION",
      //   field: "LOCATION",
      //   editable: "always", // ให้สามารถแก้ไขได้
      //   editComponent: ({ value, onChange }) => (
      //     <FormControl variant="outlined" size="small" fullWidth>
      //       <Select
      //         value={value || selectedValueLocation} // ตรวจสอบค่าปัจจุบัน
      //         onChange={(event) => {
      //           const newValue = event.target.value;
      //           setSelectedValueLocation(newValue); // อัปเดต state
      //           onChange(newValue); // อัปเดตข้อมูลใน MaterialTable
      //         }}
      //       >
      //         <MenuItem value="LS">LS</MenuItem>
      //         <MenuItem value="DC_CM">DC_CM</MenuItem>
      //         <MenuItem value="HC_PCB">HC_PCB</MenuItem>
      //         <MenuItem value="ACCOUNT">ACCOUNT</MenuItem>
      //         <MenuItem value="FM">FM</MenuItem>c{" "}
      //         <MenuItem value="OTHER_FM">OTHER_FM</MenuItem>
      //       </Select>
      //     </FormControl>
      //   ),
      // };

      const locationColumn = {
        title: "LOCATION",
        field: "LOCATION",

        // editable: "never",
        headerStyle: {
          maxWidth: 50,
          whiteSpace: "nowrap",
          textAlign: "center",
        },
        cellStyle: {
          textAlign: "center",
          borderLeft: 1,
          borderRight: 1,
          borderBottom: 1,
          borderTop: 1,
          borderColor: "#E0E0E0",
          borderStyle: "solid",
          paddingLeft: "6px",
          paddingRight: "6px",
          paddingBottom: "12px",
          paddingTop: "12px",
        },
        editComponent: (props, item) => {
          return (
            <TextField
              fullWidth
              disabled={
                (props.rowData.BM_PARENT_STS === "00" &&
                  props.rowData.GROUPID === "-") ||
                props.rowData.PAYER === "-"
                  ? false
                  : true
              }
              select
              value={props.value || "LS"}
              onChange={(event) => props.onChange(event.target.value)}
              SelectProps={{
                native: true,
              }}
            >
              {handleEval(menuItemsLocation).map((option) => (
                <option key={option.ID} value={option.VAL}>
                  {option.VAL}
                </option>
              ))}
            </TextField>
          );
        },
      };

      //todo location

      // todo invoiceid

      const voucherColumn = {
        title: "VOUCHER",
        field: "VOUCHER",
        editable: "never", // ไม่สามารถแก้ไขได้โดยตรง
      };

      // const IDColumn = {
      //   title: "GROUP_ID",
      //   field: "GROUPID",
      //   //editable: (rowData) => rowData.BM_PARENT_ID !== "-", // จะทำให้แก้ไขได้เมื่อ BM_PARENT_ID เป็น '-'
      //   editComponent: (props) => {
      //     return (
      //       // console.log(JSON.stringify(props))
      //       <div>
      //         <FormControl variant="outlined" size="small" fullWidth>
      //           <Select
      //             value={props.value}
      //             onChange={(e) => {
      //               props.onChange(e.target.value); // อัปเดตค่าเมื่อเลือก
      //             }}
      //           >
      //             <MenuItem disabled value="-">
      //               Select...
      //             </MenuItem>

      //             <MenuItem value="-">-</MenuItem>

      //             {/* เพิ่ม options ตามต้องการ */}
      //           </Select>
      //         </FormControl>
      //       </div>
      //     );
      //   },
      // };

      const IDColumn = {
        title: "GROUP_ID",
        field: "GROUPID",
        // editable: "never",
        headerStyle: {
          maxWidth: 50,
          whiteSpace: "nowrap",
          textAlign: "center",
        },
        cellStyle: {
          textAlign: "center",
          borderLeft: 1,
          borderRight: 1,
          borderBottom: 1,
          borderTop: 1,
          borderColor: "#E0E0E0",
          borderStyle: "solid",
          paddingLeft: "6px",
          paddingRight: "6px",
          paddingBottom: "12px",
          paddingTop: "12px",
        },
        editComponent: (props) => {
          // if (
          //   props.rowData.BM_PARENT_STS === "77" &&
          //   props.rowData.GROUPID === "-"
          // )

          {
            return (
              <TextField
                fullWidth
                select
                disabled={
                  props.rowData.BM_PARENT_STS === "77" &&
                  props.rowData.GROUPID === "-"
                    ? false
                    : true
                }
                value={props.value || "-"}
                onChange={(event) => props.onChange(event.target.value)}
                SelectProps={{
                  native: true,
                }}
              >
                {" "}
                <option />
                {handleEval(getinvoiceidReducer.result).map((option) => (
                  <option key={option.HC_FNNO} value={option.HC_FNNO}>
                    {option.HC_FNNO}
                  </option>
                ))}
              </TextField>
            );
          }
          //  else {
          //   return (
          //     <TextField
          //       disabled
          //       fullWidth
          //       value={props.value || "-"}
          //       SelectProps={{
          //         native: true,
          //       }}
          //     ></TextField>
          //   );
          // }
        },
      };

      // const IDColumn = {
      //   title: "GROUP_ID",
      //   field: "GROUPID",
      //   editable: "never", // ไม่สามารถแก้ไขได้โดยตรง
      // };

      // const IDColumn = {
      //   title: "GROUP_ID",
      //   field: "GROUPID",
      //   editable: false,
      //   editComponent: (props) => {
      //     return (
      //       <select
      //         value={props.value}
      //         onChange={(e) => {
      //           props.onChange(e.target.value); // อัปเดตค่าเมื่อเลือก
      //         }}
      //       >
      //         <option value="-">Select...</option>
      //         <option value="1">Group 1</option>
      //         <option value="2">Group 2</option>
      //         <option value="3">Group 3</option>
      //         {/* เพิ่ม options ตามต้องการ */}
      //       </select>
      //     );
      //   },
      //   render: (rowData) => {
      //     return rowData.BM_PARENT_ID === "-" ? (
      //       <span>-</span> // แสดงเป็น '-' ถ้าไม่ใช่โหมดแก้ไข
      //     ) : (
      //       rowData.GROUPID // แสดงค่าเดิมถ้าไม่ใช่ '-'
      //     );
      //   },
      // };

      // const handleSelectChange = (rowData, newValue) => {
      //   // ทำสิ่งที่ต้องการกับค่าใหม่ที่เลือก
      //   console.log("New Value:", newValue);
      //   // เช่น อัปเดตค่าใน state หรือส่งข้อมูลไปที่ API
      // };

      setColumns([
        ...generatedColumns,
        isSplitColumn,
        payerColumn,
        payerNameColumn,
        bankChargeColumn,
        overPayColumn,
        CNDNColumn,
        typeColumn,
        locationColumn,
        voucherColumn,
        IDColumn,

        // {
        //   title: "Action",
        //   field: "action",
        //   render: (rowData) =>
        //     rowData.BM_PARENT_ID === "xx" && rowData.GROUPID === "xx" ? (
        //       <button
        //         onClick={async () => {
        //           alert("SPLIT");
        //           alert(rowData.PAYER);
        //           let formData = new FormData();
        //           formData.append("ID", rowData.ID);
        //           formData.append("BM_CONO", rowData.BM_CONO);
        //           formData.append("PAYER", rowData.PAYER);
        //           formData.append("OVERPAY", rowData.OVERPAY);
        //           formData.append("CNDN", rowData.CNDN);
        //           formData.append("ACCNO", rowData.ACCNO);
        //           formData.append("BM_DESC", rowData.BM_DESC);
        //           formData.append("BM_DATE", rowData.BM_DATE);
        //           formData.append("BM_TIME", rowData.BM_TIME);
        //           formData.append("BANKCHARGE", rowData.BANKCHARGE);
        //           formData.append("BM_AMOUNT", rowData.BM_AMOUNT);
        //           formData.append("BM_PARENT", rowData.BM_PARENT);
        //           formData.append("GROUP_ID", rowData.GROUP_ID);
        //           formData.append("H_RNNO", rowData.H_RNNO);
        //           formData.append("OKCUNO", rowData.OKCUNO);
        //           formData.append("PAYERNAME", rowData.PAYERNAME);
        //           formData.append("TYPE", rowData.TYPE);
        //           formData.append("CHECKTYPE", selectedValue);
        //           // เพิ่มข้อมูลที่จำเป็นลงใน formData ตามที่ต้องการ
        //           // await dispatch(SplitActions.splitstatement(formData));
        //         }}
        //         style={{
        //           backgroundColor: "#3f51b5",
        //           color: "white",
        //           border: "none",
        //           padding: "5px 10px",
        //           cursor: "pointer",
        //         }}
        //       >
        //         Split
        //       </button>
        //     ) : rowData.BM_PARENT_STS === "77" ? (
        //       <DeleteForeverIcon
        //         onClick={async () => {
        //           const isConfirmed = window.confirm(
        //             "คุณต้องการจะลบข้อมูลนี้จริง ๆ ใช่ไหม?"
        //           );
        //           if (isConfirmed) {
        //             alert("Delete");

        //             alert(JSON.stringify(rowData));
        //             let formData = new FormData();
        //             formData.append("ID", rowData.ID);
        //             formData.append("BM_CONO", rowData.BM_CONO);
        //             formData.append("BM_PARENT", rowData.BM_PARENT_ID);
        //             formData.append("GROUP_ID", rowData.GROUPID);
        //             formData.append("H_RNNO", rowData.GROUPID);
        //             formData.append("CHECKTYPE", selectedValue);
        //             // เพิ่มข้อมูลที่จำเป็นลงใน formData ตามที่ต้องการ
        //             await dispatch(CreateIDActions.DELETEID(formData));
        //             await dispatch(
        //               CreateIDActions.getitem(selectedDate, selectedValue)
        //             );
        //           } else {
        //             //alert("ยกเลิกการลบ");
        //             console.log("ยกเลิกการลบ");
        //           }
        //         }}
        //         style={{ color: "red", cursor: "pointer", fontSize: 40 }}
        //       />
        //     ) : null, // ไม่แสดงปุ่มถ้าไม่ใช่ parent row
        // },
      ]);

      setTableData(createIDReducer.result);
    }
  }, [createIDReducer.result, getpayerReducer.result]);

  const formatDate = (date) => {
    const year = date.getFullYear();
    const month = ("0" + (date.getMonth() + 1)).slice(-2);
    const day = ("0" + date.getDate()).slice(-2);
    return `${year}${month}${day}`;
  };

  const [selectedDate, setSelectedDate] = useState(() => {
    const today = new Date();
    return formatDate(today);
  });

  const handleDateChange = (e) => {
    const newDate = new Date(e.target.value);
    setSelectedDate(formatDate(newDate));
  };

  const handleDropdownChange = (event) => {
    setSelectedValue(event.target.value);
    console.log("ค่าที่เลือก:", event.target.value);
  };

  const handleRowClick = (event, rowData) => {
    alert(`You clicked on ${rowData.BM_PARENT_STS}`);

    console.log(rowData);
  };

  // const handleSelectChange = (event, rowData) => {
  //   console.log("[fired]::handleSelectionChange", rowData);
  // };
  ////////////////////////////

  ////////////////////////////

  return (
    <Box
      display="flex"
      justifyContent="center" // จัดให้อยู่ตรงกลาง
      alignItems="center"
      width="100%"
      mb={3}
      flexWrap="wrap"
      style={{ marginTop: "80px" }}
    >
      <Paper className={classes.paper}>
        <Grid container justifyContent="center" spacing={1}>
          <Grid item xs={12}>
            <Box
              display="flex"
              justifyContent="flex-start" // ทำให้ Dropdown ชิดซ้าย
              alignItems="center"
              width="100%"
              mb={3}
              flexWrap="wrap"
              style={{ paddingTop: "10px", paddingLeft: "10px" }} // เพิ่ม Padding ด้านซ้ายเพื่อให้มีช่องว่าง
            >
              <div
                style={{ display: "flex", alignItems: "center", gap: "20px" }}
              >
                <FormControl variant="outlined" style={{ minWidth: 200 }}>
                  <InputLabel htmlFor="select-box">เลือกประเภท</InputLabel>
                  <Select
                    value={selectedValue}
                    onChange={handleDropdownChange}
                    label="เลือกประเภท"
                    inputProps={{
                      name: "type",
                      id: "select-box",
                    }}
                  >
                    <MenuItem value="SCB">SCB</MenuItem>
                    <MenuItem value="KBANK">KBANK</MenuItem>
                    <MenuItem value="BBL">BBL</MenuItem>
                    <MenuItem value="SCB_BILL">SCB (Bill Payment)</MenuItem>
                    <MenuItem value="KBANK_BILL">KBANK (Bill Payment)</MenuItem>
                    <MenuItem value="BBL_BILL">BBL (Bill Payment)</MenuItem>
                    <MenuItem value="SCB_MMN">SCB (MAE MANEE)</MenuItem>
                    <MenuItem value="SCB_CUR">SCB (Current)</MenuItem>
                    <MenuItem value="KBANK_CUR">KBANK (Current)</MenuItem>
                    <MenuItem value="BBL_CUR">BBL (Current)</MenuItem>
                    <MenuItem value="BBL_QR">
                      BBL (Bill Payment QR CODE)
                    </MenuItem>
                  </Select>
                </FormControl>

                <TextField
                  id="datePicker"
                  label="เลือกวันที่"
                  type="date"
                  variant="outlined" // เพิ่มกรอบเหมือน Select
                  value={
                    selectedDate
                      ? selectedDate.slice(0, 4) +
                        "-" +
                        selectedDate.slice(4, 6) +
                        "-" +
                        selectedDate.slice(6, 8)
                      : ""
                  }
                  onChange={handleDateChange}
                  InputLabelProps={{
                    shrink: true, // ทำให้ label ขยับขึ้นเมื่อมีค่า
                  }}
                  style={{ width: 210 }}
                />

                <Button
                  style={{
                    width: "150px",
                    height: "50px",
                  }}
                  variant="contained"
                  size="large"
                  color="primary"
                  onClick={async () => {
                    //alert("SEARCH");
                    //alert(selectedDate);
                    await dispatch(
                      CreateIDActions.getitem(selectedDate, selectedValue)
                    );
                  }}
                >
                  SEARCH
                </Button>
              </div>
            </Box>
          </Grid>
        </Grid>
      </Paper>

      {/* Material Table */}
      <MaterialTable
        title={selectedValue}
        columns={columns}
        data={tableData}
        //onRowClick={handleRowClick}
        options={{
          search: true,
          paging: true,
          pageSize: 18,
          pageSizeOptions: [20, 50, 100], // ตัวเลือกจำนวนแถวที่สามารถเลือกได้
          rowStyle: (rowData) => ({
            backgroundColor: rowData.BM_PARENT_ID !== "-" ? "white" : "#efe5d1", // rowcolor
          }),
        }}
        style={{ marginTop: "20px", width: "100%" }}
        editable={{
          onRowDelete: async (rowData) => {
            const isConfirmed = window.confirm(
              "คุณต้องการจะลบข้อมูลนี้จริง ๆ ใช่ไหม?"
            );
            if (isConfirmed) {
              //alert("Delete");

              //alert(JSON.stringify(rowData));
              let formData = new FormData();
              formData.append("ID", rowData.ID);
              formData.append("BM_CONO", rowData.BM_CONO);
              formData.append("BM_PARENT", rowData.BM_PARENT_ID);
              formData.append("GROUP_ID", rowData.GROUPID);
              formData.append("H_RNNO", rowData.GROUPID);
              formData.append("CHECKTYPE", selectedValue);
              // เพิ่มข้อมูลที่จำเป็นลงใน formData ตามที่ต้องการ

              try {
                await dispatch(CreateIDActions.DELETEID(formData));
                await dispatch(
                  CreateIDActions.getitem(selectedDate, selectedValue)
                );
              } catch (error) {
                console.error("Error deleting data:", error);
              }
            } else {
              // alert("ยกเลิกการลบ");
              console.log("ยกเลิกการลบ");
            }
          },
          // isEditHidden: (rowData) =>
          //   rowData.GROUPID !== "-" || rowData.PAYER === "-",
          isEditHidden: (rowData) =>
            rowData.PAYER !== "-" && rowData.GROUPID !== "-",

          isDeleteHidden: (rowData) => rowData.BM_PARENT_STS !== "77",

          onRowUpdate: (newData, oldData) =>
            new Promise(async (resolve, reject) => {
              // alert(newData.PAYER);
              // alert(oldData.PAYER);
              if (
                newData.PAYER === undefined ||
                newData.PAYER === null ||
                newData.PAYER === ""
              ) {
                alert("กรุณาเลือก PAYER ให้ถูกต้อง1");
                resolve(); // ยังคง resolve เพื่อปิดการแก้ไข
                return;
              }

              // if (
              //   oldData.PAYER === undefined ||
              //   oldData.PAYER === null ||
              //   oldData.PAYER === ""
              // ) {
              //   alert("กรุณาเลือก PAYER ให้ถูกต้อง2");
              //   resolve(); // ยังคง resolve เพื่อปิดการแก้ไข
              //   return;
              // }
              // alert(JSON.stringify(oldData));
              // alert(JSON.stringify(newData));
              // alert(JSON.stringify(oldData.BM_PARENT_STS));
              // alert(JSON.stringify(newData.BM_PARENT_STS));
              // alert(JSON.stringify(newData.ID));
              if (oldData.BM_PARENT_STS === "88") {
                //alert("HEAD1");

                if (newData.BM_PARENT_STS === "88") {
                  if (oldData.GROUPID === "-") {
                    //alert("SPLIT");

                    // alert(newData.BANKCHARGE);
                    // alert(newData.BM_AMOUNT);
                    // alert(newData.GROUP_ID);
                    // alert(newData.H_RNNO);
                    // alert(newData.OKCUNO);
                    // alert(newData.PAYERNAME);
                    // alert(newData.TYPE);
                    // alert(newData.BANKCHARGE);
                    // alert(selectedValue);

                    try {
                      let formData = new FormData();
                      formData.append("ID", oldData.ID);
                      formData.append("BM_CONO", newData.BM_CONO);
                      formData.append("PAYER", newData.PAYER);
                      formData.append("OVERPAY", newData.OVERPAY);
                      formData.append("CNDN", newData.CNDN);
                      formData.append("ACCNO", oldData.ACCNO);
                      formData.append("BM_DESC", oldData.BM_DESC);
                      formData.append("TYPE", newData.TYPE); // ใส่ TYPE ครั้งเดียว
                      formData.append("BM_DATE", oldData.BM_DATE);
                      formData.append("BM_TIME", oldData.BM_TIME);
                      formData.append("BANKCHARGE", newData.BANKCHARGE);
                      formData.append("BM_AMOUNT", oldData.BM_AMOUNT);
                      formData.append("newBM_AMOUNT", newData.BM_AMOUNT);
                      formData.append("BM_PARENT", "NONE");
                      formData.append("GROUP_ID", "-");
                      formData.append("H_RNNO", "-");
                      formData.append("OKCUNO", newData.PAYER);
                      formData.append("PAYERNAME", "-");
                      formData.append("CHECKTYPE", selectedValue); // ตรวจสอบว่ามี selectedValue หรือไม่

                      // ส่งข้อมูลไปที่ action
                      await dispatch(SplitActions.splitstatement(formData));
                      await dispatch(
                        CreateIDActions.getitem(selectedDate, selectedValue)
                      );
                      resolve();

                      console.log("Data dispatched successfully!");
                    } catch (error) {
                      console.error("Error occurred while dispatching:", error);
                    }
                  } else {
                    // alert("UPDATEHEAD333333 NO UPDATE　　");
                    alert("ไม่สามารถแก้ไขได้ กรุณาRETURN BM1");
                  }
                } else {
                  // alert("UPDATEHEAD3 NOUPDATE");
                  alert("ไม่สามารถแก้ไขได้ กรุณาRETURN BM2");
                }
              } else {
                // alert("NORMAL OR  CHILD4");

                if (newData.BM_PARENT_STS === "77") {
                  //alert("CHILD5");

                  if (oldData.GROUPID === "-") {
                    //alert("SAVEID6");

                    let formData = new FormData();

                    // alert(JSON.stringify(oldData));
                    // alert(JSON.stringify(newData));

                    formData.append("ID", oldData.ID);
                    formData.append("BM_CONO", "cono");
                    formData.append("PAYER", newData.PAYER);
                    formData.append("OVERPAY", newData.OVERPAY);
                    formData.append("CNDN", newData.CNDN);
                    formData.append("ACCNO", oldData.ACCNO);
                    formData.append("BANKCHARGE", newData.BANKCHARGE);
                    if (newData.TYPE) {
                      formData.append("TYPE", newData.TYPE);
                    } else {
                      formData.append("TYPE", "TRANSFER");
                    }
                    formData.append("CHECKTYPE", selectedValue);
                    formData.append("GROUPID", newData.GROUPID);
                    if (newData.TYPE) {
                      formData.append("TYPE", newData.TYPE);
                    } else {
                      formData.append("LOCATION", "LS");
                    }
                    // เพิ่มข้อมูลที่จำเป็นลงใน formData ตามที่ต้องการ
                    await dispatch(CreateIDActions.SAVEID(formData));
                    await dispatch(
                      CreateIDActions.getitem(selectedDate, selectedValue)
                    );
                    resolve();
                  } else {
                    //alert("UPDATE7 ---- NOUPdate");
                    alert("ไม่สามารถแก้ไขได้ กรุณาRETURN BM3");
                    resolve();
                  }
                } else {
                  //alert("NORMAL8");

                  if (newData.BM_PARENT_STS === "88") {
                    if (oldData.GROUPID === "-") {
                      //alert("SPLIT9");

                      // alert(oldData.ID);
                      // alert(JSON.stringify(oldData));
                      // alert(oldData.BM_AMOUNT);
                      // alert(newData.PAYER);
                      // alert(newData.H_RNNO);
                      // alert(newData.OKCUNO);
                      // alert(newData.PAYERNAME);
                      // alert(newData.TYPE);
                      // alert(newData.BANKCHARGE);
                      // alert(selectedValue);

                      //////////////////////////////

                      /////////////////////////////
                      try {
                        let formData = new FormData();
                        formData.append("ID", oldData.ID);
                        formData.append("BM_CONO", newData.BM_CONO);
                        formData.append("PAYER", newData.PAYER);
                        formData.append("OVERPAY", newData.OVERPAY);
                        formData.append("CNDN", newData.CNDN);
                        formData.append("ACCNO", oldData.ACCNO);
                        formData.append("BM_DESC", oldData.BM_DESC);
                        formData.append("TYPE", newData.TYPE); // ใส่ TYPE ครั้งเดียว
                        formData.append("BM_DATE", oldData.BM_DATE);
                        formData.append("BM_TIME", oldData.BM_TIME);
                        formData.append("BANKCHARGE", newData.BANKCHARGE);
                        formData.append("BM_AMOUNT", oldData.BM_AMOUNT);
                        formData.append("newBM_AMOUNT", newData.BM_AMOUNT);
                        formData.append("BM_PARENT", "NONE");
                        formData.append("GROUP_ID", "-");
                        formData.append("H_RNNO", "-");
                        formData.append("OKCUNO", newData.PAYER);
                        formData.append("PAYERNAME", "-");
                        formData.append("CHECKTYPE", selectedValue); // ตรวจสอบว่ามี selectedValue หรือไม่

                        // ส่งข้อมูลไปที่ action
                        await dispatch(SplitActions.splitstatement(formData));
                        await dispatch(
                          CreateIDActions.getitem(selectedDate, selectedValue)
                        );

                        resolve();

                        console.log("Data dispatched successfully!");
                      } catch (error) {
                        console.error(
                          "Error occurred while dispatching:",
                          error
                        );
                      }
                    } else {
                      //alert("์NOSPLIT");
                      resolve();
                    }
                  } else {
                    if (oldData.GROUPID === "-") {
                      // alert("GENID10");

                      try {
                        setTimeout(async () => {
                          //alert("update");

                          let formData = new FormData();
                          formData.append("PAYER", newData.PAYER);
                          formData.append("ID", newData.ID);
                          formData.append("BANKCHARGE", newData.BANKCHARGE);
                          formData.append("OVERPAY", newData.OVERPAY);
                          formData.append("CNDN", newData.CNDN);
                          formData.append("TYPE", selectedValueType);
                          formData.append("LOCATION", selectedValueLocation);
                          formData.append("BMDATE", newData.BM_DATE);
                          formData.append("GROUPID", newData.GROUPID);
                          formData.append("BMAMT", newData.BM_AMOUNT);

                          // รอให้แต่ละ dispatch เสร็จสิ้นก่อนทำงานถัดไป
                          await dispatch(
                            CreateIDActions.updateandgetID(formData)
                          );
                          await dispatch(
                            CreateIDActions.getitem(selectedDate, selectedValue)
                          );

                          resolve();
                        }, 600);
                      } catch (error) {
                        reject(error); // ส่ง error กลับไปใน reject
                      }

                      // try {
                      //   setTimeout(async () => {
                      //     //alert("update");

                      //     let formData = new FormData();
                      //     formData.append("PAYER", newData.PAYER);
                      //     formData.append("ID", newData.ID);
                      //     formData.append("BANKCHARGE", newData.BANKCHARGE);
                      //     formData.append("OVERPAY", newData.OVERPAY);
                      //     formData.append("CNDN", newData.CNDN);
                      //     formData.append("TYPE", selectedValueType);
                      //     formData.append("LOCATION", selectedValueLocation);
                      //     formData.append("BMDATE", newData.BM_DATE);
                      //     formData.append("GROUPID", newData.GROUPID);
                      //     formData.append("BMAMT", newData.BM_AMOUNT);

                      //     // await dispatch(
                      //     //   CreateIDActions.updateandgetID(formData)
                      //     // );
                      //     // await dispatch(
                      //     //   CreateIDActions.getitem(selectedValue)
                      //     // );

                      //     resolve();
                      //   }, 600);
                      // } catch (error) {
                      //   reject();
                      // }
                    } else {
                      //alert("UPDATE11_____NO UPDATE__");
                      // alert("UPDATE");

                      try {
                        setTimeout(async () => {
                          //alert("update");

                          let formData = new FormData();
                          formData.append("PAYER", newData.PAYER);
                          formData.append("ID", newData.ID);
                          formData.append("BANKCHARGE", newData.BANKCHARGE);
                          formData.append("OVERPAY", newData.OVERPAY);
                          formData.append("CNDN", newData.CNDN);
                          formData.append("TYPE", selectedValueType);
                          formData.append("LOCATION", selectedValueLocation);
                          formData.append("BMDATE", newData.BM_DATE);
                          formData.append("GROUPID", oldData.GROUPID);
                          formData.append("BMAMT", newData.BM_AMOUNT);

                          // รอให้แต่ละ dispatch เสร็จสิ้นก่อนทำงานถัดไป
                          await dispatch(CreateIDActions.updateonly(formData));
                          await dispatch(
                            CreateIDActions.getitem(selectedDate, selectedValue)
                          );

                          resolve();
                        }, 600);
                      } catch (error) {
                        reject(error); // ส่ง error กลับไปใน reject
                      }

                      resolve();
                    }
                  }
                }
              }

              // if (oldData.BM_PARENT_STS !== "88") {
              //   // update action

              //   if (oldData.BM_PARENT_ID === "-") {
              //     if (newData.BM_PARENT_STS === "88") {
              //       alert("split action");
              //     } else {
              //       alert("genid action");
              //     }
              /*



                  try {
                    // การตั้งเวลา delay โดยไม่ใช้ async/await ใน setTimeout
                    setTimeout(async () => {
                      alert("update");
                      // const data = [...tableData];
                      // data[oldData.tableData.id] = newData;
                      // setTableData(data);

                      // alert(JSON.stringify(newData));
                      //  alert(JSON.stringify(oldData));

                      // alert(newData.ID);
                      // alert(newData.BANKCHARGE);
                      // alert(newData.OVERPAY);
                      // alert(newData.CNDN);
                      // alert(newData.TYPE);
                      // alert(newData.LOCATION);

                      let formData = new FormData();
                      formData.append("PAYER", newData.PAYER);
                      formData.append("ID", newData.ID);
                      formData.append("BANKCHARGE", newData.BANKCHARGE);
                      formData.append("OVERPAY", newData.OVERPAY);
                      formData.append("CNDN", newData.CNDN);
                      formData.append("TYPE", selectedValueType);
                      formData.append("LOCATION", selectedValueLocation);
                      formData.append("BMDATE", newData.BM_DATE);
                      formData.append("GROUPID", newData.GROUPID);
                      formData.append("BMAMT", newData.BM_AMOUNT);

                      await dispatch(CreateIDActions.updateandgetID(formData));
                      await dispatch(CreateIDActions.getitem(selectedValue));

                      // await dispatch(
                      //   CreateIDActions.fetchgetitem(selectedValue, selectedValue)
                      // );

                      //  await dispatch(CreateIDActions.getitem(selectedValue));

                      resolve();
                    }, 600);
                  } catch (error) {
                    reject();
                  }
                    */
              //     } else {
              //       if (oldData.BM_PARENT_STS === "77") {
              //         alert("SAVEID action");
              //       } else {
              //         alert("GENID action");
              //       }
              //     }
              //   } else {
              //     // split action
              //     if (oldData.BM_PARENT_ID === "-") {
              //       alert("update action");
              //     } else {
              //       alert("split action");
              //     }
              //   }
            }),
        }}
        // actions={[
        //   {
        //     icon: "edit",
        //     tooltip: "Edit row",
        //     iconProps: { color: "secondary" },
        //     onClick: (event, rowData) => {
        //       // เปิดโหมดแก้ไขแถวที่เลือก
        //     },
        //   },
        // ]}
        parentChildData={(row, rows) =>
          rows.find((a) => a.ID === row.BM_PARENT_ID)
        }
      />

      {/* <MaterialTable
        title="Basic Tree Data Preview"
        style={{ marginTop: "20px", width: "100%" }}
        data={tableData}
        columns={[
          { title: "ID", field: "ID" },
          { title: "ACCNO", field: "ACCNO" },
          { title: "BM_DESC", field: "BM_DESC" },
          { title: "BM_DATE", field: "BM_DATE" },
          { title: "BM_TIME", field: "BM_TIME" },
          { title: "BM_AMOUNT", field: "BM_AMOUNT" },
          { title: "BM_PARENT_ID", field: "BM_PARENT_ID" },
          { title: "BM_PARENT_STS", field: "BM_PARENT_STS" },
          {
            title: "Action",
            field: "action",
            render: (rowData) => (
              <button
                onClick={async () => {
                  alert("SPLIT");
                  alert(rowData.ID);
                  alert(selectedValue);
                  let formData = new FormData();
                  formData.append("ID", rowData.ID);
                  formData.append("BM_CONO", rowData.BM_CONO);
                  formData.append("PAYER", rowData.PAYER);
                  formData.append("OVERPAY", rowData.OVERPAY);
                  formData.append("CNDN", rowData.CNDN);
                  formData.append("ACCNO", rowData.ACCNO);
                  formData.append("BM_DESC", rowData.BM_DESC);
                  formData.append("BM_DATE", rowData.BM_DATE);
                  formData.append("BM_TIME", rowData.BM_TIME);
                  formData.append("BANKCHARGE", rowData.BANKCHARGE);
                  formData.append("BM_AMOUNT", rowData.BM_AMOUNT);
                  formData.append("BM_PARENT", rowData.BM_PARENT);
                  formData.append("GROUP_ID", rowData.GROUP_ID);
                  formData.append("H_RNNO", rowData.H_RNNO);
                  formData.append("OKCUNO", rowData.OKCUNO);
                  formData.append("PAYERNAME", rowData.PAYERNAME);
                  formData.append("TYPE", rowData.TYPE);
                  formData.append("CHECKTYPE", selectedValue);

                  // formData.append("vLocation", locationhead);
                  await dispatch(SplitActions.splitstatement(formData));
                }}
                style={{
                  backgroundColor: "#3f51b5",
                  color: "white",
                  border: "none",
                  padding: "5px 10px",
                  cursor: "pointer",
                }}
              >
                split
              </button>
            ),
          },
        ]}
        parentChildData={(row, rows) =>
          rows.find((a) => a.ID === row.BM_PARENT_ID)
        }
        options={{
          selection: true,
        }}
      /> */}
    </Box>
  );
}

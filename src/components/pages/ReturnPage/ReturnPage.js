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
      //alert(JSON.stringify(createIDReducer.result[0]));
      const generatedColumns = Object.keys(createIDReducer.result[0] || {})
        .filter((key) => !hiddenColumnsSCB.includes(key)) // กรองคีย์ที่ไม่ต้องการออก
        .map((key) => ({
          title: key,
          field: key,
          //editable: "never",
          editable: "never", // ตรวจสอบว่าเป็น BM_AMOUNT หรือไม่
        }));

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
                props.rowData.BM_PARENT_STS === "77" &&
                props.rowData.GROUPID === "-"
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
              disabled={
                (props.rowData.BM_PARENT_STS === "00" &&
                  props.rowData.GROUPID === "-") ||
                props.rowData.PAYER === "-"
                  ? false
                  : true
              }
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
              disabled={
                (props.rowData.BM_PARENT_STS === "00" &&
                  props.rowData.GROUPID === "-") ||
                props.rowData.PAYER === "-"
                  ? false
                  : true
              }
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
              disabled={
                (props.rowData.BM_PARENT_STS === "00" &&
                  props.rowData.GROUPID === "-") ||
                props.rowData.PAYER === "-"
                  ? false
                  : true
              }
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
              disabled={
                (props.rowData.BM_PARENT_STS === "00" &&
                  props.rowData.GROUPID === "-") ||
                props.rowData.PAYER === "-"
                  ? false
                  : true
              }
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
                      // setIsSpit(event.target.checked); // กำหนดให้ setIsSplit("true") เมื่อมีการเปลี่ยนแปลง
                      // alert(isSplit);
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
        },
      };

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

        {
          title: "Action",
          field: "action",
          render: (rowData) =>
            rowData.BM_PARENT_STS !== "77" ? ( // ใส่เงื่อนไขที่คุณต้องการ
              <button
                onClick={async () => {
                  const isConfirmed = window.confirm(
                    "คุณต้องการจะ RETURN ข้อมูลนี้จริง ๆ ใช่ไหม?"
                  );

                  if (isConfirmed) {
                    //  alert(JSON.stringify(rowData.BM_PARENT_STS));

                    let formData = new FormData();

                    if (rowData.BM_PARENT_STS === "00") {
                      // alert("์NORMAL");

                      formData.append("ID", rowData.ID);
                      formData.append("GROUPID", rowData.GROUPID);
                      formData.append("CONO", rowData.BM_CONO);

                      await dispatch(CreateIDActions.RETURNNORMAL(formData));

                      await dispatch(
                        CreateIDActions.getitem(selectedDate, selectedValue)
                      );

                      // alert(rowData.ID);
                      // alert(rowData.GROUPID);
                      // alert(rowData.BM_CONO);
                    } else if (rowData.BM_PARENT_STS == "88") {
                      // alert("PARENT");

                      formData.append("ID", rowData.ID);
                      formData.append("CONO", rowData.BM_CONO);

                      await dispatch(CreateIDActions.RETURNPARENT(formData));

                      await dispatch(
                        CreateIDActions.getitem(selectedDate, selectedValue)
                      );
                    } else {
                      alert("ERROR");
                    }

                    alert("RETURNED");
                  } else {
                    console.log("ยกเลิกการ RETURN");
                  }
                }}
                style={{
                  backgroundColor: "#3f51b5",
                  color: "white",
                  border: "none",
                  padding: "5px 10px",
                  cursor: "pointer",
                }}
              >
                RETURN
              </button>
            ) : null, // ถ้าเงื่อนไขไม่ตรง ให้คืนค่า null เพื่อไม่ให้แสดงปุ่ม
        },
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
                  color="primary"
                  size="large"
                  onClick={async () => {
                    ///alert("SEARCH");
                    ////alert(selectedDate);
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
        parentChildData={(row, rows) =>
          rows.find((a) => a.ID === row.BM_PARENT_ID)
        }
      />
    </Box>
  );
}

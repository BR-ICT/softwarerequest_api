import React, { useEffect, useState, useMemo } from "react";
import { useSelector, useDispatch } from "react-redux";
import moment from "moment";
import NumberFormat from "react-number-format";
import MaterialTable, { MTableToolbar } from "material-table";
import {
  makeStyles,
  withStyles,
  ThemeProvider,
  createMuiTheme,
} from "@material-ui/core/styles";
import { Typography, Grid, Paper, TextField, Button } from "@material-ui/core";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogTitle from "@material-ui/core/DialogTitle";
import CircularProgress from "@material-ui/core/CircularProgress";
import SearchIcon from "@material-ui/icons/Search";
import CancelIcon from "@material-ui/icons/Cancel";
import MailOutlineIcon from "@material-ui/icons/MailOutline";
import TextFieldsIcon from "@material-ui/icons/TextFields";
import Autocomplete from "@material-ui/lab/Autocomplete";
import TextareaAutosize from "@material-ui/core/TextareaAutosize";
import LocalAtmIcon from "@material-ui/icons/LocalAtm";
import Draggable from "react-draggable";
import { Formik, Form, Field } from "formik";
import { red, green, purple } from "@material-ui/core/colors/";
import * as prnumberActions from "./../../../actions/prnumber.action";
import * as prheadActions from "./../../../actions/prhead.action";
import * as prdetailActions from "./../../../actions/prdetail.action";
import * as textm3Actions from "./../../../actions/textm3.action";
import * as chargeActions from "./../../../actions/charge.action";
import * as expenitureActions from "./../../../actions/expeniture.action";

const useStyles = makeStyles((theme) => ({
  root: {
    width: "100%",
    marginTop: 60,
  },
  wrapper: {
    margin: theme.spacing(1),
    position: "relative",
  },
  paper: {
    padding: theme.spacing(2),
    color: theme.palette.text.secondary,
  },
  margin: {
    marginTop: "0.4rem",
    marginRight: "0.4rem",
    margin: theme.spacing(0.3),
  },
  extendedIcon: {
    marginRight: theme.spacing(1),
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

function PaperComponent(props) {
  return (
    <Draggable
      handle="#draggable-dialog-title"
      cancel={'[class*="MuiDialogContent-root"]'}
    >
      <Paper {...props} />
    </Draggable>
  );
}

export default (props) => {
  const classes = useStyles();
  const dispatch = useDispatch();
  const prnumberReducer = useSelector(({ prnumberReducer }) => prnumberReducer);
  const warehouseReducer = useSelector(
    ({ warehouseReducer }) => warehouseReducer
  );
  const departmentReducer = useSelector(
    ({ departmentReducer }) => departmentReducer
  );
  const buReducer = useSelector(({ buReducer }) => buReducer);
  const costcenterReducer = useSelector(
    ({ costcenterReducer }) => costcenterReducer
  );
  const monthReducer = useSelector(({ monthReducer }) => monthReducer);
  const chargeReducer = useSelector(({ chargeReducer }) => chargeReducer);
  const prheadReducer = useSelector(({ prheadReducer }) => prheadReducer);
  const prdetailReducer = useSelector(({ prdetailReducer }) => prdetailReducer);
  const textm3Reducer = useSelector(({ textm3Reducer }) => textm3Reducer);
  const expenitureReducer = useSelector(
    ({ expenitureReducer }) => expenitureReducer
  );

  const initialStatePRNumber = {
    vPRSelectNumber: null,
    vPRNumberDesc: null,
    vWarehouse: null,
    vBU: null,
    vDepartment: null,
    vCostCenter: null,
    vPHGroup: null,
    vMonth: null,
    vStatus: null,
  };
  const [prnumber, setPRNumber] = useState(initialStatePRNumber);
  const initialStatePRHead = {
    vPRNumber: "",
    vDate: moment(new Date()).format("YYYY-MM-DD"),
    vWarehouse: "",
    vDepartment: "",
    vMonth: "",
    vPlanUnPlan: "",
    vBU: "",
    vBuyer: "",
    vGroup: "",
    vCAPNo: "",
    vRequestor: "",
    vRemark: "",
    vApprove1: "",
    vApprove2: "",
    vApprove3: "",
    vApprove4: "",
    vStatus: "",
  };
  const [prhead, setPRHead] = useState(initialStatePRHead);
  const initialStateItemPRDetail = {
    vPRNumber: "",
    vItemLine: "",
    vItemNo: "",
    vItemDesc1: "",
    vItemDesc2: null,
    vQty: "",
    vUnit: "",
    vDateDetail: moment(new Date()).format("YYYY-MM-DD"), //"2018-12-01"
    vSupplierNo: "",
    vSupplierName: "",
    vSupplierDesc: null,
    vPrice: "",
    vVat: "",
    vCurrency: "",
    vOrdertype: "",
    vTotal: "",
    vCostcenterDetail: "",
    vPHGroupDetail: "",
    vBuyerDetail: "",
    vRemarkDetail: "",
    vTextID: "",
    vTextLine: "",
    vChargeLine: "",
    vCode: "",
    vAmount: "",
  };
  const [itemprdetail, setItemPRDetail] = useState(initialStateItemPRDetail);
  const [textm3, setTextM3] = useState("\n\n\n\n\n\n\n\n\n");

  const [search, setSearch] = useState(false);
  const [cancle, setCancle] = useState(false);
  const [openDialog, setOpenDialog] = useState(false);
  const [openDialogText, setOpenDialogText] = useState(false);
  const [openDialogCharge, setOpenDialogCharge] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  useEffect(() => {
    dispatch(prheadActions.getPOHeads());
    dispatch(chargeActions.getCharges());
    prheadReducer.result = null;
    prdetailReducer.result = null;
  }, []);

  useEffect(() => {
    const textm3s = textm3Reducer.result ? textm3Reducer.result : [];
    // console.log(JSON.stringify(prdetails));

    let texts = "";
    textm3s.map((item) => {
      // console.log(JSON.stringify(item.TLTX60));
      texts += item.TLTX60 + "\n";
    });

    for (var i = texts.split("\n").length; i < textm3.split("\n").length; i++) {
      texts += "\n";
    }

    setTextM3(texts);
  }, [textm3Reducer]);

  const charges = useMemo(() =>
    chargeReducer.result ? chargeReducer.result : []
  );

  const handleClose = () => {
    setItemPRDetail(initialStateItemPRDetail);
    setOpenDialog(false);
  };

  const handleCloseDialog = () => {
    setOpenDialogText(false);
    setOpenDialogCharge(false);
  };

  const handleTextM3 = (text) => {
    let getTexts = [text].toString().split("\n");
    // console.log(getTexts);

    if (getTexts.length > 0) {
      let sumTexts = "";
      getTexts.map((item, index) => {
        // console.log(index);
        // console.log(JSON.stringify(item));
        sumTexts += item + ";";
      });
      // alert(sumTexts);
      return sumTexts;
    }
    return null;
  };

  const showDialog = ({
    values,
    handleChange,
    handleSubmit,
    setFieldValue,
    isSubmitting,
  }) => {
    if (selectedProduct === null) {
      return "";
    }

    return (
      <Dialog
        open={openDialog}
        fullWidth={true}
        maxWidth={"lg"}
        scroll="paper"
        keepMounted
        onClose={() => {}}
        aria-labelledby="alert-dialog-slide-title"
        aria-describedby="alert-dialog-slide-description"
      >
        <form onSubmit={handleSubmit}>
          {/* <DialogTitle id="alert-dialog-slide-title">
            MPR Number : {itemprdetail.vPRNumber}
          </DialogTitle> */}
          <DialogContent>
            <MaterialTable
              id="root_prdetail"
              title={`PO Detail : ${itemprdetail.vPRNumber}`}
              columns={columnsdetail}
              data={prdetailReducer.result ? prdetailReducer.result : []}
              // components={{
              //   Toolbar: (props) => (
              //     <div>
              //       <MTableToolbar {...props} />
              //       <Grid className={(classes.margin, classes.wrapper)}>
              //         <Button
              //           // className={classes.wrapper}
              //           fullWidth
              //           disabled={sendemaildisable}
              //           variant="contained"
              //           color="primary"
              //           startIcon={<TextFieldsIcon />}
              //           onClick={(event, rowData) => {
              //             setOpenDialogText(true);
              //           }}
              //         >
              //           M3 Text
              //         </Button>
              //         {loading && (
              //           <CircularProgress
              //             size={24}
              //             className={classes.buttonProgress}
              //           />
              //         )}
              //       </Grid>
              //     </div>
              //   ),
              // }}
              options={{
                exportButton: true,
                // toolbar: false,
                paging: true,
                headerStyle: {
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
                  // backgroundColor: "red",
                  // padding: "5px",
                  // whiteSpace: "normal",
                  // wordWrap: "break-word",
                  // wordBreak: "break-all"
                },
                fixedColumns: {
                  // left: 2
                },
              }}
              actions={[
                (rowData) => ({
                  icon: TextFieldsIcon,
                  tooltip: "TextM3",
                  iconProps: { color: "primary" },
                  onClick: (event, rowData) => {
                    // console.log("rowData: " + JSON.stringify([rowData]));
                    let data = [rowData];
                    data.map((item) => {
                      setItemPRDetail({
                        ...itemprdetail,
                        vTextID: item.IBTXID,
                        vTextLine: item.IBPNLI,
                      });
                      dispatch(textm3Actions.getTextM3s(item.IBTXID));
                    });

                    setOpenDialogText(true);
                  },
                }),
                (rowData) => ({
                  icon: LocalAtmIcon,
                  tooltip: "Charge",
                  iconProps: { color: "primary" },
                  onClick: (event, rowData) => {
                    // console.log("rowData: " + JSON.stringify([rowData]));
                    let data = [rowData];
                    data.map((item) => {
                      setItemPRDetail({
                        ...itemprdetail,
                        vTextID: item.IBTXID,
                        vTextLine: item.IBPNLI,
                      });
                      dispatch(
                        expenitureActions.getExpenitures(
                          itemprdetail.vPRNumber,
                          item.IBPNLI
                        )
                      );
                    });

                    setOpenDialogCharge(true);
                  },
                }),
              ]}
            />
          </DialogContent>
          <DialogActions>
            <Button onClick={handleClose} color="secondary">
              Cancel
            </Button>
          </DialogActions>
        </form>
      </Dialog>
    );
  };

  const showDialogText = ({
    values,
    handleChange,
    handleSubmit,
    setFieldValue,
    isSubmitting,
  }) => {
    if (selectedProduct === null) {
      return "";
    }

    return (
      <Dialog
        open={openDialogText}
        fullWidth={true}
        maxWidth={"sm"}
        onClose={() => {}}
        PaperComponent={PaperComponent}
        aria-labelledby="draggable-dialog-title"
      >
        <DialogTitle style={{ cursor: "move" }} id="draggable-dialog-title">
          {`Text M3 Line : ${itemprdetail.vTextLine}`}
          <p>#Debug {JSON.stringify(textm3)}</p>
        </DialogTitle>
        <DialogContent>
          <DialogContentText>
            <TextareaAutosize
              style={{ width: "100%" }}
              fullWidth
              minRows={18}
              maxRows={18}
              aria-label="maximum height"
              placeholder="Maximum 4 rows"
              //     defaultValue="Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt
              // ut labore et dolore magna aliqua."
              value={textm3}
              onChange={(event) => {
                // console.log(event.target.value);
                setTextM3(event.target.value);
              }}
            />
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button autoFocus onClick={handleCloseDialog} color="secondary">
            Cancel
          </Button>
          <Button
            onClick={() => {
              // alert(textm3);
              let texts = handleTextM3(textm3);
              // alert(texts);
              dispatch(
                textm3Actions.updateTextM3(
                  itemprdetail.vPRNumber,
                  itemprdetail.vTextLine,
                  texts
                )
              );
            }}
            color="primary"
          >
            Save
          </Button>
        </DialogActions>
      </Dialog>
    );
  };

  const showDialogCharge = ({
    values,
    handleChange,
    handleSubmit,
    setFieldValue,
    isSubmitting,
  }) => {
    if (selectedProduct === null) {
      return "";
    }

    return (
      <Dialog
        open={openDialogCharge}
        fullWidth={true}
        maxWidth={"sm"}
        onClose={() => {}}
        PaperComponent={PaperComponent}
        aria-labelledby="draggable-dialog-title"
      >
        <DialogTitle style={{ cursor: "move" }} id="draggable-dialog-title">
          {`Charge Line : ${itemprdetail.vTextLine}`}
          {/* <p>#Debug {JSON.stringify(textm3)}</p> */}
        </DialogTitle>
        <DialogContent>
          <Grid container style={{ marginBottom: 1 }} spacing={1}>
            <Grid item sm={2}>
              <TextField
                // className={classes.margin}
                // style={{ width: "200px" }}
                required
                type="number"
                error={true}
                size="small"
                variant="outlined"
                margin="normal"
                id="vChargeLine"
                label="Line"
                onChange={(event) => {
                  // console.log(event.target.value);
                  setItemPRDetail({
                    ...itemprdetail,
                    vChargeLine: event.target.value,
                  });
                }}
                InputLabelProps={{ shrink: true, required: true }}
              ></TextField>
            </Grid>

            <Grid item sm={5}>
              <TextField
                // className={classes.margin}
                // style={{ width: "200px" }}
                required
                error={true}
                select
                size="small"
                variant="outlined"
                margin="normal"
                id="vCode"
                label="Code"
                // disabled={cancelpoDisable}
                // value={prhead.vPayment}
                // values={(values.vPayment = prhead.vPayment)}
                onChange={(event) => {
                  // console.log(event.target.value);
                  setItemPRDetail({
                    ...itemprdetail,
                    vCode: event.target.value,
                  });
                }}
                InputLabelProps={{ shrink: true }}
                SelectProps={{
                  native: true,
                }}
              >
                <option />
                {charges.map((option) => (
                  <option key={option.ID} value={option.INCEID}>
                    {option.CHARGE}
                  </option>
                ))}
              </TextField>
            </Grid>

            <Grid item sm={3}>
              <TextField
                // className={classes.margin}
                // style={{ width: "200px" }}
                required
                type="number"
                error={true}
                size="small"
                variant="outlined"
                margin="normal"
                id="vAmount"
                label="Amount"
                onChange={(event) => {
                  // console.log(event.target.value);
                  setItemPRDetail({
                    ...itemprdetail,
                    vAmount: event.target.value,
                  });
                }}
                InputLabelProps={{ shrink: true, required: true }}
              />
            </Grid>
            <Grid sm={1} style={{ alignSelf: "center" }}>
              <Button
                // fullWidth
                size="medium"
                id="vAdd"
                variant="contained"
                color="primary"
                // startIcon={<SearchIcon />}
                onClick={() => {
                  dispatch(
                    expenitureActions.updateCharge3(
                      itemprdetail.vPRNumber,
                      itemprdetail.vTextLine,
                      itemprdetail.vChargeLine,
                      itemprdetail.vCode,
                      itemprdetail.vAmount
                    )
                  );
                }}
              >
                Add
              </Button>
            </Grid>
          </Grid>

          <DialogContentText>
            <MaterialTable
              id="root_prdetail"
              title={`PO Charge : ${itemprdetail.vPRNumber}`}
              columns={columnscharge}
              data={expenitureReducer.result ? expenitureReducer.result : []}
              options={{
                exportButton: false,
                toolbar: false,
                paging: true,
                headerStyle: {
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
                  // backgroundColor: "red",
                  // padding: "5px",
                  // whiteSpace: "normal",
                  // wordWrap: "break-word",
                  // wordBreak: "break-all"
                },
                fixedColumns: {
                  // left: 2
                },
              }}
            />
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button autoFocus onClick={handleCloseDialog} color="secondary">
            Cancel
          </Button>
        </DialogActions>
      </Dialog>
    );
  };

  const columns = [
    {
      title: "PR Number",
      field: "IAPUNO",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.IAPUNO}
        </Typography>
      ),
    },
    {
      title: "Status F",
      field: "IAPUSL",
      headerStyle: { maxWidth: 70, whiteSpace: "nowrap", textAlign: "center" },
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.IAPUSL}
        </Typography>
      ),
    },
    {
      title: "Status B",
      field: "IAPUST",
      headerStyle: { maxWidth: 70, whiteSpace: "nowrap", textAlign: "center" },
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.IAPUST}
        </Typography>
      ),
    },
    {
      title: "Whs",
      field: "IAWHLO",
      headerStyle: { maxWidth: 70, whiteSpace: "nowrap", textAlign: "center" },
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.IAWHLO}
        </Typography>
      ),
    },
    {
      title: "Order Date",
      field: "HD_PURCDT",
      type: "date",
      headerStyle: { maxWidth: 150, whiteSpace: "nowrap", textAlign: "center" },
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {moment(item.HD_PURCDT).format("DD/MM/YYYY")}
        </Typography>
      ),
    },
    {
      title: "Buyer",
      field: "IABUYE",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
      cellStyle: {
        textAlign: "left",
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.IABUYE}
        </Typography>
      ),
    },
    {
      title: "Order type",
      field: "IAORTY",
      headerStyle: { maxWidth: 70, whiteSpace: "nowrap", textAlign: "center" },
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.IAORTY}
        </Typography>
      ),
    },

    {
      title: "Suplier No.",
      field: "IASUNO",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.IASUNO}
        </Typography>
      ),
    },
    {
      title: "Suplier Name",
      field: "SASUNM",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
      cellStyle: {
        textAlign: "left",
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.SASUNM}
        </Typography>
      ),
    },
    {
      title: "Ref1",
      field: "IAYRE1",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
      cellStyle: {
        textAlign: "left",
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.IAYRE1}
        </Typography>
      ),
    },
  ];

  const columnsdetail = [
    {
      title: "Line",
      field: "IBPNLI",
      // type: "numeric",
      editable: "never",
      width: 50,
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.IBPNLI}
        </Typography>
      ),
    },
    {
      title: "Status",
      field: "IBPUSL",
      // type: "numeric",
      editable: "never",
      width: 50,
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.IBPUSL}
        </Typography>
      ),
    },
    {
      title: "Item No",
      field: "IBITNO",
      editable: "never",
      headerStyle: { maxWidth: 150, whiteSpace: "nowrap", textAlign: "center" },
      cellStyle: {
        textAlign: "left",
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
      // editComponent: props => (
      //   <Autocomplete
      //     id="combo-box-demo"
      //     options={top100Films}
      //     getOptionLabel={option => option.title}
      //     style={{ width: 100 }}
      //     renderInput={params => <TextField {...params} />}
      //   />
      // ),
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.IBITNO}
        </Typography>
      ),
    },
    {
      title: "Item Name",
      field: "IBPITD",
      editable: "never",
      headerStyle: { maxWidth: 150, whiteSpace: "nowrap", textAlign: "center" },
      cellStyle: {
        textAlign: "left",
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.IBPITD}
        </Typography>
      ),
    },
    {
      title: "Qty",
      field: "IBORQA",
      editable: "never",
      type: "numeric",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
      cellStyle: {
        textAlign: "right",
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {/* {item.PR_IBORQA} */}
          {/* var NumberFormat = require('react-number-format'); */}
          <NumberFormat
            value={item.IBORQA}
            displayType={"text"}
            thousandSeparator={true}
            // prefix={"$"}
          />
        </Typography>
      ),
    },
    {
      title: "Unit",
      field: "IBPUUN",
      editable: "never",
      headerStyle: { maxWidth: 50, whiteSpace: "nowrap", textAlign: "center" },
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.IBPUUN}
        </Typography>
      ),
    },
    {
      title: "Conf dely date",
      field: "IBCODT",
      type: "date",
      headerStyle: { maxWidth: 150, whiteSpace: "nowrap", textAlign: "center" },
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.IBCODT === "0--"
            ? moment(item.IADWDT).format("DD/MM/YYYY")
            : moment(item.IBCODT).format("DD/MM/YYYY")}
        </Typography>
      ),
    },
    {
      title: "Text ID",
      field: "IBTXID",
      type: "text",
      headerStyle: { maxWidth: 70, whiteSpace: "nowrap", textAlign: "center" },
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.IBTXID}
        </Typography>
      ),
    },
  ];

  const columnscharge = [
    {
      title: "Line",
      field: "IVCDSE",
      // type: "numeric",
      editable: "never",
      width: 50,
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.IVCDSE}
        </Typography>
      ),
    },
    {
      title: "Item Line",
      field: "IVPNLI",
      // type: "numeric",
      editable: "never",
      width: 50,
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.IVPNLI}
        </Typography>
      ),
    },
    {
      title: "Code",
      field: "IVCEID",
      // type: "numeric",
      editable: "never",
      width: 50,
      headerStyle: {
        maxWidth: 50,
        whiteSpace: "nowrap",
        textAlign: "center",
      },
      cellStyle: {
        textAlign: "left",
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.IVCEID}
        </Typography>
      ),
    },
    {
      title: "Name",
      field: "INTX30",
      // type: "numeric",
      editable: "never",
      width: 70,
      headerStyle: {
        maxWidth: 70,
        whiteSpace: "nowrap",
        textAlign: "center",
      },
      cellStyle: {
        textAlign: "left",
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.INTX30}
        </Typography>
      ),
    },

    {
      title: "Amount",
      field: "IVCEVJ",
      // type: "numeric",
      editable: "never",
      width: 100,
      headerStyle: {
        maxWidth: 100,
        whiteSpace: "nowrap",
        textAlign: "center",
      },
      cellStyle: {
        textAlign: "right",
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.IVCEVJ}
        </Typography>
      ),
    },
    {
      title: "Currency",
      field: "IVCUCD",
      // type: "numeric",
      editable: "never",
      width: 50,
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
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.IVCUCD}
        </Typography>
      ),
    },
  ];

  return (
    <div className={classes.root}>
      {/* Grid */}
      {/* <p>#Debug prnumber {JSON.stringify(prnumber)}</p> */}
      {/* <Formik initialValues="">{(props) => showForm(props)}</Formik> */}

      {/* Plan PR Table */}
      <MaterialTable
        id="root_pr"
        title={`PO Monitoring`}
        columns={columns}
        data={prheadReducer.result ? prheadReducer.result : []}
        options={{
          // exportButton: true,
          // toolbar: false,
          pageSize: 10,
          paging: true,
          headerStyle: {
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
            // backgroundColor: "red",
            // whiteSpace: "normal",
            // wordWrap: "break-word",
            // wordBreak: "break-all"
          },
          fixedColumns: {
            // left: 2
          },
        }}
        actions={[
          (rowData) => ({
            icon: "search",
            tooltip: "Search row",
            iconProps: { color: "primary" },
            onClick: (event, rowData) => {
              // console.log("rowData: " + JSON.stringify([rowData]));
              let data = [rowData];
              data.map((item) => {
                setItemPRDetail({
                  ...itemprdetail,
                  vPRNumber: item.IAPUNO,
                  vStatus: item.HD_STATUS,
                });
                dispatch(prdetailActions.getPODetailsMonitoring(item.IAPUNO));
              });
              setSelectedProduct("rowData");
              setOpenDialog(true);
            },
          }),
        ]}
      />

      {/* Dialog */}
      <Formik>{(props) => showDialog(props)}</Formik>

      {/* DialogText */}
      <Formik>{(props) => showDialogText(props)}</Formik>

      {/* DialogCharge */}
      <Formik>{(props) => showDialogCharge(props)}</Formik>
    </div>
  );
};

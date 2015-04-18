package org.nprogramming.fiximulator2.ui;

import org.nprogramming.fiximulator2.api.*;
import org.nprogramming.fiximulator2.data.*;
import org.nprogramming.fiximulator2.domain.Execution;
import org.nprogramming.fiximulator2.domain.IOI;
import org.nprogramming.fiximulator2.domain.Order;
import org.nprogramming.fiximulator2.fix.FIXimulator;
import org.nprogramming.fiximulator2.ui.tables.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class FIXimulatorFrame extends javax.swing.JFrame {

    private static FIXimulator fiximulator;

    private final transient OrderRepositoryWithCallback orderRepository;
    private final transient RepositoryWithCallback<Execution> executionRepository;
    private final transient RepositoryWithCallback<IOI> ioiRepository;
    private final transient InstrumentsApi instrumentsApi;

    private transient IOI dialogIOI = null;
    private transient Execution dialogExecution = null;

    private transient IOITableModel ioiDataModel;
    private transient ExecutionTableModel executionTableModel;
    private transient OrderTableModel orderTableModel;

    private static Logger LOG = LoggerFactory.getLogger(FIXimulatorFrame.class);

    public FIXimulatorFrame(
            OrderRepositoryWithCallback orderRepository,
            RepositoryWithCallback<Execution> executionRepository,
            RepositoryWithCallback<IOI> ioiRepository,
            InstrumentsApi instrumentsApi
    ) {
        this.orderRepository = orderRepository;
        this.executionRepository = executionRepository;
        this.ioiRepository = ioiRepository;
        this.instrumentsApi = instrumentsApi;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        org.jdesktop.beansbinding.BindingGroup bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        aboutDialog = new JDialog();
        JPanel aboutPanel = new JPanel();
        JButton okButton = new JButton();
        JLabel aboutDialogLabel = new JLabel();
        instrumentFileChooser = new JFileChooser();
        ioiDialog = new JDialog();
        JButton ioiDialogOK = new JButton();
        JButton ioiDialogCancel = new JButton();
        JLabel ioiIDLabel = new JLabel();
        JLabel ioiSideLabel = new JLabel();
        JLabel ioiSharesLabel = new JLabel();
        JLabel ioiSymbolLabel = new JLabel();
        JLabel ioiSecurityIDLabel = new JLabel();
        JLabel ioiIDSourceLabel = new JLabel();
        JLabel ioiPriceLabel = new JLabel();
        JLabel ioiNaturalLabel = new JLabel();
        ioiDialogID = new JLabel();
        ioiDialogSide = new JComboBox();
        ioiDialogSymbol = new JTextField();
        ioiDialogSecurityID = new JTextField();
        ioiDialogIDSource = new JComboBox();
        ioiDialogNatural = new JComboBox();
        ioiDialogShares = new JFormattedTextField();
        ioiDialogPrice = new JFormattedTextField();
        executionDialog = new JDialog();
        JButton executionDialogOK = new JButton();
        JButton executionDialogCancel = new JButton();
        executionDialogShares = new JFormattedTextField();
        executionDialogPrice = new JFormattedTextField();
        JLabel executionSharesLabel = new JLabel();
        JLabel executionPriceLabel = new JLabel();
        JPanel messagePanel = new JPanel();
        JScrollPane messageScrollPane = new JScrollPane();
        JTable messageTable = new JTable();
        JPanel statusBarPanel = new JPanel();
        JLabel executorRunningLabel = new JLabel();
        JLabel ioiSenderRunningLabel = new JLabel();
        JLabel clientConnectedLabel = new JLabel();
        JPanel messageDetailPanel = new JPanel();
        JScrollPane messageDetailScrollPane = new JScrollPane();
        JTable messageDetailTable = new JTable();
        JTabbedPane mainTabbedPane = new JTabbedPane();
        JPanel loadPanel = new JPanel();
        JPanel autoIOIPanel = new JPanel();
        securityIDComboBox = new JComboBox();
        rateSlider = new JSlider();
        JLabel rateDisplayLable = new JLabel();
        JLabel symbolLabel = new JLabel();
        JButton stopButton = new JButton();
        JButton startButton = new JButton();
        symbolComboBox = new JComboBox();
        JLabel securityIDLabel = new JLabel();
        JLabel ioiSliderLabel = new JLabel();
        JPanel autoExecutePanel = new JPanel();
        JButton stopExecutorButton = new JButton();
        partialsSlider = new JSlider();
        JLabel partialsLabel = new JLabel();
        JLabel partialsNumber = new JLabel();
        JButton startExecutorButton = new JButton();
        JLabel delayLabel = new JLabel();
        executorDelay = new JComboBox();
        JPanel ioiPanel = new JPanel();
        JPanel manualIOIPanel = new JPanel();
        JButton singleIOIButton = new JButton();
        JButton cancelIOIButton = new JButton();
        JButton replaceIOIButton = new JButton();
        JScrollPane ioiScrollPane = new JScrollPane();
        ioiTable = new JTable();
        JPanel orderPanel = new JPanel();
        JPanel orderActionPanel = new JPanel();
        JButton acknowledgeButton = new JButton();
        JButton cancelButton = new JButton();
        JButton cancelPendingButton = new JButton();
        JButton replacePendingButton = new JButton();
        JButton executeButton = new JButton();
        JButton dfdButton = new JButton();
        JButton cancelAcceptButton = new JButton();
        JButton replaceAcceptButton = new JButton();
        JButton orderRejectButton = new JButton();
        JButton cancelRejectButton = new JButton();
        JButton replaceRejectButton = new JButton();
        JScrollPane orderScrollPane = new JScrollPane();
        orderTable = new JTable();
        JPanel executionPanel = new JPanel();
        JPanel executionActionPanel = new JPanel();
        JButton executionBustButton = new JButton();
        JButton executionCorrectButton = new JButton();
        JScrollPane executionScrollPane = new JScrollPane();
        executionTable = new JTable();
        JPanel instrumentPanel = new JPanel();
        JScrollPane instrumentScrollPane = new JScrollPane();
        JTable instrumentTable = new JTable();
        JPanel reportPanel = new JPanel();
        JPanel reportActionPanel = new JPanel();
        JButton customQueryRunButton = new JButton();
        JLabel queryLabel = new JLabel();
        queryText = new JTextField();
        cannedQueryCombo = new JComboBox();
        JLabel querySymbolLabel = new JLabel();
        querySymbolText = new JTextField();
        JButton cannedQueryRunButton = new JButton();
        JScrollPane reportScrollPane = new JScrollPane();
        reportTable = new JTable();
        JPanel settingsPanel = new JPanel();
        JPanel autoResponsePanel = new JPanel();
        autoAcknowledge = new JCheckBox();
        autoPendingCancel = new JCheckBox();
        autoPendingReplace = new JCheckBox();
        autoCancel = new JCheckBox();
        autoReplace = new JCheckBox();
        JSeparator cancelSeparator = new JSeparator();
        JSeparator replaceSeparator = new JSeparator();
        JButton saveSettingsButton = new JButton();
        JPanel appSettingsPanel = new JPanel();
        JLabel pricePrecisionLabel = new JLabel();
        JLabel cachedObjectsLabel = new JLabel();
        JComboBox cachedObjectsCombo = new JComboBox();
        pricePrecisionCombo = new JComboBox();
        JSeparator oboCompIDSeparator = new JSeparator();
        sendOnBehalfOfCompID = new JCheckBox();
        sendOnBehalfOfSubID = new JCheckBox();
        JSeparator oboCompIDSeparator1 = new JSeparator();
        JLabel logToFileLabel = new JLabel();
        JCheckBox logToFile = new JCheckBox();
        JCheckBox logToDB = new JCheckBox();
        JButton showSettingsButton = new JButton();
        JMenuBar mainMenuBar = new JMenuBar();
        JMenu fileMenu = new JMenu();
        JMenu instrumentMenu = new JMenu();
        JMenuItem loadInstrumentMenuItem = new JMenuItem();
        JMenu helpMenu = new JMenu();
        JMenuItem aboutMenuItem = new JMenuItem();

        aboutDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        aboutDialog.setTitle("About...");
        aboutDialog.setLocationByPlatform(true);

        aboutPanel.setPreferredSize(new java.awt.Dimension(200, 100));

        okButton.setText("OK");
        okButton.addActionListener(this::okButtonActionPerformed);

        aboutDialogLabel.setText("FIXimulator by Zoltan Feledy");

        javax.swing.GroupLayout aboutPanelLayout = new javax.swing.GroupLayout(aboutPanel);
        aboutPanel.setLayout(aboutPanelLayout);
        aboutPanelLayout.setHorizontalGroup(
                aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(aboutPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(okButton)
                                        .addComponent(aboutDialogLabel))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        aboutPanelLayout.setVerticalGroup(
                aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(aboutPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(aboutDialogLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(okButton)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout aboutDialogLayout = new javax.swing.GroupLayout(aboutDialog.getContentPane());
        aboutDialog.getContentPane().setLayout(aboutDialogLayout);
        aboutDialogLayout.setHorizontalGroup(
                aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(aboutDialogLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(aboutPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                                .addContainerGap())
        );
        aboutDialogLayout.setVerticalGroup(
                aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aboutDialogLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(aboutPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7))
        );

        ioiDialog.setTitle("Add IOI...");
        ioiDialog.setAlwaysOnTop(true);
        ioiDialog.setName("ioiDialog"); // NOI18N

        ioiDialogOK.setText("OK");
        ioiDialogOK.addActionListener(this::ioiDialogOKActionPerformed);

        ioiDialogCancel.setText("Cancel");
        ioiDialogCancel.addActionListener(this::ioiDialogCancelActionPerformed);

        ioiIDLabel.setText("IOIid(23):");

        ioiSideLabel.setText("Side(54):");

        ioiSharesLabel.setText("IOIShares(27):");

        ioiSymbolLabel.setText("Symbol(55):");

        ioiSecurityIDLabel.setText("SecurityID(48):");

        ioiIDSourceLabel.setText("IDSource(22):");

        ioiPriceLabel.setText("Price(44):");

        ioiNaturalLabel.setText("IOINaturalFlag(130):");

        ioiDialogID.setText("ioiIDLabel");

        ioiDialogSide.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Buy", "Sell", "Undisclosed"}));

        ioiDialogIDSource.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"CUSIP", "SEDOL", "RIC", "TICKER", "OTHER"}));

        ioiDialogNatural.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Yes", "No"}));

        ioiDialogShares.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        ioiDialogPrice.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.####"))));
        ioiDialogPrice.setText("0.0");

        javax.swing.GroupLayout ioiDialogLayout = new javax.swing.GroupLayout(ioiDialog.getContentPane());
        ioiDialog.getContentPane().setLayout(ioiDialogLayout);
        ioiDialogLayout.setHorizontalGroup(
                ioiDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ioiDialogLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(ioiDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(ioiDialogOK)
                                        .addComponent(ioiNaturalLabel)
                                        .addComponent(ioiIDLabel)
                                        .addComponent(ioiSideLabel)
                                        .addComponent(ioiSharesLabel)
                                        .addComponent(ioiSymbolLabel)
                                        .addComponent(ioiSecurityIDLabel)
                                        .addComponent(ioiIDSourceLabel)
                                        .addComponent(ioiPriceLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(ioiDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ioiDialogSide, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ioiDialogSymbol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ioiDialogID)
                                        .addComponent(ioiDialogSecurityID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ioiDialogIDSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ioiDialogNatural, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ioiDialogCancel)
                                        .addComponent(ioiDialogShares, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ioiDialogPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(38, Short.MAX_VALUE))
        );

        ioiDialogLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, ioiDialogCancel, ioiDialogOK);

        ioiDialogLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, ioiIDLabel, ioiIDSourceLabel, ioiNaturalLabel, ioiPriceLabel, ioiSecurityIDLabel, ioiSharesLabel, ioiSideLabel, ioiSymbolLabel);

        ioiDialogLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, ioiDialogID, ioiDialogIDSource, ioiDialogNatural, ioiDialogPrice, ioiDialogSecurityID, ioiDialogShares, ioiDialogSide, ioiDialogSymbol);

        ioiDialogLayout.setVerticalGroup(
                ioiDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ioiDialogLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(ioiDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ioiIDLabel)
                                        .addComponent(ioiDialogID, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(ioiDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ioiSideLabel)
                                        .addComponent(ioiDialogSide, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(ioiDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ioiSharesLabel)
                                        .addComponent(ioiDialogShares, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(ioiDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ioiSymbolLabel)
                                        .addComponent(ioiDialogSymbol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(ioiDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ioiSecurityIDLabel)
                                        .addComponent(ioiDialogSecurityID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(ioiDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ioiIDSourceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ioiDialogIDSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(ioiDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ioiPriceLabel)
                                        .addComponent(ioiDialogPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(ioiDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ioiNaturalLabel)
                                        .addComponent(ioiDialogNatural, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(ioiDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ioiDialogOK)
                                        .addComponent(ioiDialogCancel))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        executionDialog.setTitle("Add execution...");
        executionDialog.setName("executionDialog"); // NOI18N

        executionDialogOK.setText("OK");
        executionDialogOK.addActionListener(this::executionDialogOKActionPerformed);

        executionDialogCancel.setText("Cancel");
        executionDialogCancel.addActionListener(this::executionDialogCancelActionPerformed);

        executionDialogShares.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        executionDialogPrice.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.####"))));
        executionDialogPrice.setText("0.0");

        executionSharesLabel.setText("LastShares(32):");

        executionPriceLabel.setText("LastPx(31):");

        javax.swing.GroupLayout executionDialogLayout = new javax.swing.GroupLayout(executionDialog.getContentPane());
        executionDialog.getContentPane().setLayout(executionDialogLayout);
        executionDialogLayout.setHorizontalGroup(
                executionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(executionDialogLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(executionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(executionDialogLayout.createSequentialGroup()
                                                .addGroup(executionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(executionPriceLabel)
                                                        .addComponent(executionSharesLabel))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(executionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(executionDialogPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                                                        .addComponent(executionDialogShares, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, executionDialogLayout.createSequentialGroup()
                                                .addComponent(executionDialogOK)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(executionDialogCancel)))
                                .addContainerGap())
        );
        executionDialogLayout.setVerticalGroup(
                executionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(executionDialogLayout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(executionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(executionSharesLabel)
                                        .addComponent(executionDialogShares, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(executionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(executionPriceLabel)
                                        .addComponent(executionDialogPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addGroup(executionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(executionDialogOK)
                                        .addComponent(executionDialogCancel))
                                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FIXimulator");
        setBounds(new java.awt.Rectangle(50, 50, 0, 0));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setName("fiximulatorFrame"); // NOI18N
        setResizable(true);

        messagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Application Messages"));

        messageTable.setAutoCreateRowSorter(true);
        messageTable.setModel(new MessageTableModel());
        messageTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //Set initial column widths
        for (int i = 0; i < messageTable.getColumnCount(); i++) {
            if (i == 0)
                messageTable.getColumnModel().
                        getColumn(i).setPreferredWidth(30);
            if (i == 1)
                messageTable.getColumnModel().
                        getColumn(i).setPreferredWidth(75);
            if (i == 2)
                messageTable.getColumnModel().
                        getColumn(i).setPreferredWidth(150);
            if (i == 3)
                messageTable.getColumnModel().
                        getColumn(i).setPreferredWidth(150);
            if (i == 4)
                messageTable.getColumnModel().
                        getColumn(i).setPreferredWidth(800);
        }
        messageScrollPane.setViewportView(messageTable);

        javax.swing.GroupLayout messagePanelLayout = new javax.swing.GroupLayout(messagePanel);
        messagePanel.setLayout(messagePanelLayout);
        messagePanelLayout.setHorizontalGroup(
                messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(messageScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
        );
        messagePanelLayout.setVerticalGroup(
                messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(messageScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
        );

        executorRunningLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/red.gif"))); // NOI18N
        executorRunningLabel.setText("Executor status");
        FIXimulator.getApplication().addStatusCallbacks(
                new JLabelStatusSwitcher(clientConnectedLabel),
                new JLabelStatusSwitcher(ioiSenderRunningLabel),
                new JLabelStatusSwitcher(executorRunningLabel)
        );

        ioiSenderRunningLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/red.gif"))); // NOI18N
        ioiSenderRunningLabel.setText("IOI sender status");

        clientConnectedLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/red.gif"))); // NOI18N
        if (FIXimulator.getApplication().getConnectionStatus())
            clientConnectedLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/green.gif")));
        clientConnectedLabel.setText("Client connection status");

        javax.swing.GroupLayout statusBarPanelLayout = new javax.swing.GroupLayout(statusBarPanel);
        statusBarPanel.setLayout(statusBarPanelLayout);
        statusBarPanelLayout.setHorizontalGroup(
                statusBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(statusBarPanelLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(clientConnectedLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ioiSenderRunningLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(executorRunningLabel)
                                .addContainerGap(69, Short.MAX_VALUE))
        );
        statusBarPanelLayout.setVerticalGroup(
                statusBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(statusBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(clientConnectedLabel)
                                .addComponent(ioiSenderRunningLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(executorRunningLabel))
        );

        messageDetailPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Message Details"));

        messageDetailTable.setAutoCreateRowSorter(true);
        messageDetailTable.setModel(new MessageDetailTableModel(messageTable));
        messageDetailTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < messageDetailTable.getColumnCount(); i++) {
            if (i == 0)
                messageDetailTable.getColumnModel().
                        getColumn(i).setPreferredWidth(100);
            if (i == 1)
                messageDetailTable.getColumnModel().
                        getColumn(i).setPreferredWidth(40);
            if (i == 2)
                messageDetailTable.getColumnModel().
                        getColumn(i).setPreferredWidth(150);
            if (i == 3)
                messageDetailTable.getColumnModel().
                        getColumn(i).setPreferredWidth(150);
        }
        messageDetailScrollPane.setViewportView(messageDetailTable);

        javax.swing.GroupLayout messageDetailPanelLayout = new javax.swing.GroupLayout(messageDetailPanel);
        messageDetailPanel.setLayout(messageDetailPanelLayout);
        messageDetailPanelLayout.setHorizontalGroup(
                messageDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(messageDetailScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
        );
        messageDetailPanelLayout.setVerticalGroup(
                messageDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(messageDetailPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(messageDetailScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE))
        );

        autoIOIPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Automated IOI Sender"));

        securityIDComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"RIC", "Sedol", "RIC", "Cusip"}));
        securityIDComboBox.addActionListener(this::securityIDComboBoxActionPerformed);

        rateSlider.setMajorTickSpacing(200);
        rateSlider.setMaximum(600);
        rateSlider.setMinorTickSpacing(50);
        rateSlider.setPaintLabels(true);
        rateSlider.setPaintTicks(true);
        rateSlider.setValue(60);
        rateSlider.addChangeListener(this::sliderChanged);

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rateSlider, org.jdesktop.beansbinding.ELProperty.create("${value}"), rateDisplayLable, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        symbolLabel.setText("Symbol (55):");

        stopButton.setText("Stop");
        stopButton.addActionListener(FIXimulatorFrame.this::stopButtonActionPerformed);

        startButton.setText("Start");
        startButton.addActionListener(FIXimulatorFrame.this::startButtonActionPerformed);

        symbolComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Ticker", "Sedol", "RIC", "Cusip"}));
        symbolComboBox.addActionListener(FIXimulatorFrame.this::symbolComboBoxActionPerformed);

        securityIDLabel.setText("SecurityID (48):");

        ioiSliderLabel.setText(" IOIs per minute:");

        javax.swing.GroupLayout autoIOIPanelLayout = new javax.swing.GroupLayout(autoIOIPanel);
        autoIOIPanel.setLayout(autoIOIPanelLayout);
        autoIOIPanelLayout.setHorizontalGroup(
                autoIOIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(autoIOIPanelLayout.createSequentialGroup()
                                .addGroup(autoIOIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(autoIOIPanelLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(startButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(stopButton))
                                        .addGroup(autoIOIPanelLayout.createSequentialGroup()
                                                .addComponent(ioiSliderLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(rateDisplayLable, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(autoIOIPanelLayout.createSequentialGroup()
                                                .addComponent(symbolLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(symbolComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(securityIDLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(securityIDComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(rateSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE))
                                .addContainerGap())
        );

        autoIOIPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, startButton, stopButton);

        autoIOIPanelLayout.setVerticalGroup(
                autoIOIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, autoIOIPanelLayout.createSequentialGroup()
                                .addGroup(autoIOIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(symbolLabel)
                                        .addComponent(symbolComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(securityIDLabel)
                                        .addComponent(securityIDComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(autoIOIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ioiSliderLabel)
                                        .addComponent(rateDisplayLable, javax.swing.GroupLayout.DEFAULT_SIZE, 14, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rateSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(autoIOIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(startButton)
                                        .addComponent(stopButton)))
        );

        autoExecutePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Automated Executor"));

        stopExecutorButton.setText("Stop");
        stopExecutorButton.addActionListener(FIXimulatorFrame.this::stopExecutorButtonActionPerformed);

        partialsSlider.setMajorTickSpacing(10);
        partialsSlider.setMaximum(50);
        partialsSlider.setPaintLabels(true);
        partialsSlider.setPaintTicks(true);
        partialsSlider.setValue(10);
        partialsSlider.addChangeListener(FIXimulatorFrame.this::partialsSliderChanged);

        partialsLabel.setText("Fills per order:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, partialsSlider, org.jdesktop.beansbinding.ELProperty.create("${value}"), partialsNumber, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        startExecutorButton.setText("Start");
        startExecutorButton.addActionListener(this::startExecutorButtonActionPerformed);

        delayLabel.setText("Delay:");

        executorDelay.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"1 ms", "10 ms", "100 ms", "1 second", "5 seconds"}));
        executorDelay.addActionListener(FIXimulatorFrame.this::executorDelayActionPerformed);

        javax.swing.GroupLayout autoExecutePanelLayout = new javax.swing.GroupLayout(autoExecutePanel);
        autoExecutePanel.setLayout(autoExecutePanelLayout);
        autoExecutePanelLayout.setHorizontalGroup(
                autoExecutePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(autoExecutePanelLayout.createSequentialGroup()
                                .addGroup(autoExecutePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(autoExecutePanelLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(autoExecutePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(autoExecutePanelLayout.createSequentialGroup()
                                                                .addComponent(delayLabel)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(executorDelay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(partialsLabel)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(partialsNumber))
                                                        .addGroup(autoExecutePanelLayout.createSequentialGroup()
                                                                .addComponent(startExecutorButton)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(stopExecutorButton))))
                                        .addComponent(partialsSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE))
                                .addContainerGap())
        );

        autoExecutePanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, startExecutorButton, stopExecutorButton);

        autoExecutePanelLayout.setVerticalGroup(
                autoExecutePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(autoExecutePanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(autoExecutePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(delayLabel)
                                        .addComponent(executorDelay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(partialsNumber)
                                        .addComponent(partialsLabel))
                                .addGap(11, 11, 11)
                                .addComponent(partialsSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(autoExecutePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(stopExecutorButton)
                                        .addComponent(startExecutorButton))
                                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout loadPanelLayout = new javax.swing.GroupLayout(loadPanel);
        loadPanel.setLayout(loadPanelLayout);
        loadPanelLayout.setHorizontalGroup(
                loadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loadPanelLayout.createSequentialGroup()
                                .addGroup(loadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(autoExecutePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(autoIOIPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        loadPanelLayout.setVerticalGroup(
                loadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(loadPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(autoIOIPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(autoExecutePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainTabbedPane.addTab("Load", loadPanel);

        manualIOIPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("IOIs"));

        singleIOIButton.setText("Add IOI");
        singleIOIButton.addActionListener(this::singleIOIButtonActionPerformed);

        cancelIOIButton.setText("Cancel IOI");
        cancelIOIButton.addActionListener(FIXimulatorFrame.this::cancelIOIButtonActionPerformed);

        replaceIOIButton.setText("Replace IOI");
        replaceIOIButton.addActionListener(FIXimulatorFrame.this::replaceIOIButtonActionPerformed);

        javax.swing.GroupLayout manualIOIPanelLayout = new javax.swing.GroupLayout(manualIOIPanel);
        manualIOIPanel.setLayout(manualIOIPanelLayout);
        manualIOIPanelLayout.setHorizontalGroup(
                manualIOIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(manualIOIPanelLayout.createSequentialGroup()
                                .addComponent(singleIOIButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(replaceIOIButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cancelIOIButton)
                                .addContainerGap(140, Short.MAX_VALUE))
        );

        manualIOIPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, cancelIOIButton, replaceIOIButton, singleIOIButton);

        manualIOIPanelLayout.setVerticalGroup(
                manualIOIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(manualIOIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(singleIOIButton)
                                .addComponent(replaceIOIButton)
                                .addComponent(cancelIOIButton))
        );

        ioiTable.setDefaultRenderer(Object.class, new IOICellRenderer());
        ioiTable.setAutoCreateRowSorter(true);
        ioiDataModel = new IOITableModel(ioiRepository);
        ioiTable.setModel(ioiDataModel);
        ioiTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //Set initial column widths
        for (int i = 0; i < ioiTable.getColumnCount(); i++) {
            if (i == 0)
                ioiTable.getColumnModel().
                        getColumn(i).setPreferredWidth(100);
            if (i == 1)
                ioiTable.getColumnModel().
                        getColumn(i).setPreferredWidth(60);
            if (i == 2)
                ioiTable.getColumnModel().
                        getColumn(i).setPreferredWidth(50);
            if (i == 3)
                ioiTable.getColumnModel().
                        getColumn(i).setPreferredWidth(50);
            if (i == 4)
                ioiTable.getColumnModel().
                        getColumn(i).setPreferredWidth(50);
            if (i == 5)
                ioiTable.getColumnModel().
                        getColumn(i).setPreferredWidth(60);
            if (i == 6)
                ioiTable.getColumnModel().
                        getColumn(i).setPreferredWidth(60);
            if (i == 7)
                ioiTable.getColumnModel().
                        getColumn(i).setPreferredWidth(60);
            if (i == 8)
                ioiTable.getColumnModel().
                        getColumn(i).setPreferredWidth(50);
            if (i == 9)
                ioiTable.getColumnModel().
                        getColumn(i).setPreferredWidth(100);
        }
        ioiTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        ioiScrollPane.setViewportView(ioiTable);

        javax.swing.GroupLayout ioiPanelLayout = new javax.swing.GroupLayout(ioiPanel);
        ioiPanel.setLayout(ioiPanelLayout);
        ioiPanelLayout.setHorizontalGroup(
                ioiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ioiPanelLayout.createSequentialGroup()
                                .addComponent(manualIOIPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(ioiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(ioiScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE))
        );
        ioiPanelLayout.setVerticalGroup(
                ioiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ioiPanelLayout.createSequentialGroup()
                                .addComponent(manualIOIPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(258, Short.MAX_VALUE))
                        .addGroup(ioiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(ioiPanelLayout.createSequentialGroup()
                                        .addGap(54, 54, 54)
                                        .addComponent(ioiScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)))
        );

        mainTabbedPane.addTab("IOIs", ioiPanel);

        orderActionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Orders"));

        acknowledgeButton.setText("Acknowledge");
        acknowledgeButton.addActionListener(FIXimulatorFrame.this::acknowledgeButtonActionPerformed);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(FIXimulatorFrame.this::cancelButtonActionPerformed);

        cancelPendingButton.setText("Pending Cancel");
        cancelPendingButton.addActionListener(FIXimulatorFrame.this::cancelPendingButtonActionPerformed);

        replacePendingButton.setText("Pending Replace");
        replacePendingButton.addActionListener(FIXimulatorFrame.this::replacePendingButtonActionPerformed);

        executeButton.setText("Execute");
        executeButton.addActionListener(FIXimulatorFrame.this::executeButtonActionPerformed);

        dfdButton.setText("DFD");
        dfdButton.addActionListener(FIXimulatorFrame.this::dfdButtonActionPerformed);

        cancelAcceptButton.setText("Accept Cancel");
        cancelAcceptButton.addActionListener(FIXimulatorFrame.this::cancelAcceptButtonActionPerformed);

        replaceAcceptButton.setText("Accept Replace");
        replaceAcceptButton.addActionListener(FIXimulatorFrame.this::replaceAcceptButtonActionPerformed);

        orderRejectButton.setText("Reject");
        orderRejectButton.addActionListener(FIXimulatorFrame.this::orderRejectButtonActionPerformed);

        cancelRejectButton.setText("Reject Cancel");
        cancelRejectButton.addActionListener(FIXimulatorFrame.this::cancelRejectButtonActionPerformed);

        replaceRejectButton.setText("Reject Replace");
        replaceRejectButton.addActionListener(FIXimulatorFrame.this::replaceRejectButtonActionPerformed);

        javax.swing.GroupLayout orderActionPanelLayout = new javax.swing.GroupLayout(orderActionPanel);
        orderActionPanel.setLayout(orderActionPanelLayout);
        orderActionPanelLayout.setHorizontalGroup(
                orderActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, orderActionPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(orderActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addGroup(orderActionPanelLayout.createSequentialGroup()
                                                .addComponent(orderRejectButton, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                                                .addGap(6, 6, 6)
                                                .addComponent(dfdButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(orderActionPanelLayout.createSequentialGroup()
                                                .addComponent(acknowledgeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(6, 6, 6)
                                                .addComponent(cancelButton))
                                        .addComponent(executeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(orderActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(cancelRejectButton)
                                        .addComponent(cancelAcceptButton)
                                        .addComponent(cancelPendingButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(orderActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(replacePendingButton)
                                        .addComponent(replaceAcceptButton)
                                        .addComponent(replaceRejectButton))
                                .addContainerGap())
        );

        orderActionPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, acknowledgeButton, orderRejectButton);

        orderActionPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, replaceAcceptButton, replacePendingButton, replaceRejectButton);

        orderActionPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, cancelAcceptButton, cancelPendingButton, cancelRejectButton);

        orderActionPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, cancelButton, dfdButton);

        orderActionPanelLayout.setVerticalGroup(
                orderActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(orderActionPanelLayout.createSequentialGroup()
                                .addGroup(orderActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(orderActionPanelLayout.createSequentialGroup()
                                                .addGroup(orderActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(cancelPendingButton)
                                                        .addGroup(orderActionPanelLayout.createSequentialGroup()
                                                                .addGap(29, 29, 29)
                                                                .addComponent(cancelAcceptButton))
                                                        .addGroup(orderActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(cancelButton)
                                                                .addComponent(acknowledgeButton))
                                                        .addGroup(orderActionPanelLayout.createSequentialGroup()
                                                                .addGap(29, 29, 29)
                                                                .addGroup(orderActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(orderRejectButton)
                                                                        .addComponent(dfdButton))))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(orderActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(cancelRejectButton)
                                                        .addComponent(replaceRejectButton)
                                                        .addComponent(executeButton)))
                                        .addGroup(orderActionPanelLayout.createSequentialGroup()
                                                .addComponent(replacePendingButton)
                                                .addGap(6, 6, 6)
                                                .addComponent(replaceAcceptButton)))
                                .addContainerGap())
        );

        orderTable.setAutoCreateRowSorter(true);
        orderTableModel = new OrderTableModel(orderRepository);
        orderTable.setModel(orderTableModel);
        orderTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //Set initial column widths
        for (int i = 0; i < orderTable.getColumnCount(); i++) {
            if (i == 0) // Order ID
                orderTable.getColumnModel().
                        getColumn(i).setPreferredWidth(90);
            if (i == 1) // Status
                orderTable.getColumnModel().
                        getColumn(i).setPreferredWidth(100);
            if (i == 2) // Side
                orderTable.getColumnModel().
                        getColumn(i).setPreferredWidth(30);
            if (i == 3)
                orderTable.getColumnModel().
                        getColumn(i).setPreferredWidth(60);
            if (i == 4)
                orderTable.getColumnModel().
                        getColumn(i).setPreferredWidth(50);
            if (i == 5)
                orderTable.getColumnModel().
                        getColumn(i).setPreferredWidth(80);
            if (i == 6) // Limit
                orderTable.getColumnModel().
                        getColumn(i).setPreferredWidth(50);
            if (i == 7) // TIF
                orderTable.getColumnModel().
                        getColumn(i).setPreferredWidth(30);
            if (i == 8) // Executed
                orderTable.getColumnModel().
                        getColumn(i).setPreferredWidth(70);
            if (i == 9) // Open
                orderTable.getColumnModel().
                        getColumn(i).setPreferredWidth(50);
            if (i == 10) // AvgPx
                orderTable.getColumnModel().
                        getColumn(i).setPreferredWidth(50);
            if (i == 11) // ClOrdID
                orderTable.getColumnModel().
                        getColumn(i).setPreferredWidth(90);
            if (i == 12) // OrigClOrdID
                orderTable.getColumnModel().
                        getColumn(i).setPreferredWidth(90);
        }
        orderTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        orderScrollPane.setViewportView(orderTable);

        javax.swing.GroupLayout orderPanelLayout = new javax.swing.GroupLayout(orderPanel);
        orderPanel.setLayout(orderPanelLayout);
        orderPanelLayout.setHorizontalGroup(
                orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(orderScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                        .addGroup(orderPanelLayout.createSequentialGroup()
                                .addComponent(orderActionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        orderPanelLayout.setVerticalGroup(
                orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(orderPanelLayout.createSequentialGroup()
                                .addComponent(orderActionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(orderScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
        );

        mainTabbedPane.addTab("Orders", orderPanel);

        executionActionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Executions"));

        executionBustButton.setText("Bust");
        executionBustButton.addActionListener(FIXimulatorFrame.this::executionBustButtonActionPerformed);

        executionCorrectButton.setText("Correct");
        executionCorrectButton.addActionListener(FIXimulatorFrame.this::executionCorrectButtonActionPerformed);

        javax.swing.GroupLayout executionActionPanelLayout = new javax.swing.GroupLayout(executionActionPanel);
        executionActionPanel.setLayout(executionActionPanelLayout);
        executionActionPanelLayout.setHorizontalGroup(
                executionActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(executionActionPanelLayout.createSequentialGroup()
                                .addComponent(executionBustButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(executionCorrectButton)
                                .addGap(228, 228, 228))
        );
        executionActionPanelLayout.setVerticalGroup(
                executionActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(executionActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(executionBustButton)
                                .addComponent(executionCorrectButton))
        );

        executionTable.setDefaultRenderer(Object.class, new ExecutionCellRenderer());
        executionTable.setAutoCreateRowSorter(true);
        executionTableModel = new ExecutionTableModel(executionRepository);
        executionTable.setModel(executionTableModel);
        executionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //Set initial column widths
        for (int i = 0; i < executionTable.getColumnCount(); i++) {
            if (i == 0) // ID
                executionTable.getColumnModel().
                        getColumn(i).setPreferredWidth(90);
            if (i == 1) // ClOrdID
                executionTable.getColumnModel().
                        getColumn(i).setPreferredWidth(90);
            if (i == 2) // Side
                executionTable.getColumnModel().
                        getColumn(i).setPreferredWidth(30);
            if (i == 3) // Sybol
                executionTable.getColumnModel().
                        getColumn(i).setPreferredWidth(60);
            if (i == 4) // LastQty
                executionTable.getColumnModel().
                        getColumn(i).setPreferredWidth(50);
            if (i == 5) // LastPx
                executionTable.getColumnModel().
                        getColumn(i).setPreferredWidth(50);
            if (i == 6) // CumQty
                executionTable.getColumnModel().
                        getColumn(i).setPreferredWidth(50);
            if (i == 7) // AvgPx
                executionTable.getColumnModel().
                        getColumn(i).setPreferredWidth(50);
            if (i == 8) // Open
                executionTable.getColumnModel().
                        getColumn(i).setPreferredWidth(50);
            if (i == 9) // ExecType
                executionTable.getColumnModel().
                        getColumn(i).setPreferredWidth(70);
            if (i == 10) // ExecTransType
                executionTable.getColumnModel().
                        getColumn(i).setPreferredWidth(70);
            if (i == 11) // RefID
                executionTable.getColumnModel().
                        getColumn(i).setPreferredWidth(90);
        }
        executionTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        executionScrollPane.setViewportView(executionTable);

        javax.swing.GroupLayout executionPanelLayout = new javax.swing.GroupLayout(executionPanel);
        executionPanel.setLayout(executionPanelLayout);
        executionPanelLayout.setHorizontalGroup(
                executionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(executionActionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                        .addComponent(executionScrollPane, 0, 0, Short.MAX_VALUE)
        );
        executionPanelLayout.setVerticalGroup(
                executionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(executionPanelLayout.createSequentialGroup()
                                .addComponent(executionActionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(executionScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))
        );

        mainTabbedPane.addTab("Executions", executionPanel);

        instrumentTable.setAutoCreateRowSorter(true);
        instrumentTable.setModel(new InstrumentTableModel(instrumentsApi));
        instrumentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //Set initial column widths
        for (int i = 0; i < instrumentTable.getColumnCount(); i++) {
            if (i == 0)
                instrumentTable.getColumnModel().
                        getColumn(i).setPreferredWidth(50);
            if (i == 1)
                instrumentTable.getColumnModel().
                        getColumn(i).setPreferredWidth(200);
        }
        instrumentTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        instrumentScrollPane.setViewportView(instrumentTable);

        javax.swing.GroupLayout instrumentPanelLayout = new javax.swing.GroupLayout(instrumentPanel);
        instrumentPanel.setLayout(instrumentPanelLayout);
        instrumentPanelLayout.setHorizontalGroup(
                instrumentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(instrumentScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
        );
        instrumentPanelLayout.setVerticalGroup(
                instrumentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(instrumentPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(instrumentScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
        );

        mainTabbedPane.addTab("Instruments", instrumentPanel);

        reportActionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Reporting"));

        customQueryRunButton.setText("Run");
        customQueryRunButton.addActionListener(this::customQueryRunButtonActionPerformed);

        queryLabel.setText("Query:");

        queryText.setText("select text from messages_log where text like '%35=6%';");

        cannedQueryCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Show last 10 IOIs...", "Show last 10 orders...", "Show last 10 executions...", "Show all IOIs where Symbol(55) is...", "Show all orders where Symbol(55) is...", "Show all executions where Symbol(55) is...", "Show all activity where Symbol(55) is..."}));

        querySymbolLabel.setText("Symbol:");

        cannedQueryRunButton.setText("Run");
        cannedQueryRunButton.addActionListener(this::cannedQueryRunButtonActionPerformed);

        javax.swing.GroupLayout reportActionPanelLayout = new javax.swing.GroupLayout(reportActionPanel);
        reportActionPanel.setLayout(reportActionPanelLayout);
        reportActionPanelLayout.setHorizontalGroup(
                reportActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(reportActionPanelLayout.createSequentialGroup()
                                .addGroup(reportActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(reportActionPanelLayout.createSequentialGroup()
                                                .addComponent(cannedQueryCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(querySymbolLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(querySymbolText, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, reportActionPanelLayout.createSequentialGroup()
                                                .addComponent(queryLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(queryText, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(reportActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(cannedQueryRunButton)
                                        .addComponent(customQueryRunButton))
                                .addContainerGap())
        );
        reportActionPanelLayout.setVerticalGroup(
                reportActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(reportActionPanelLayout.createSequentialGroup()
                                .addGroup(reportActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(queryLabel)
                                        .addComponent(customQueryRunButton)
                                        .addComponent(queryText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(reportActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cannedQueryRunButton)
                                        .addComponent(querySymbolText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(querySymbolLabel)
                                        .addComponent(cannedQueryCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        executionTable.setDefaultRenderer(Object.class, new ExecutionCellRenderer());
        reportTable.setAutoCreateRowSorter(true);
        reportTable.setModel(new QueryTableModel());
        reportTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        reportTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        reportScrollPane.setViewportView(reportTable);

        javax.swing.GroupLayout reportPanelLayout = new javax.swing.GroupLayout(reportPanel);
        reportPanel.setLayout(reportPanelLayout);
        reportPanelLayout.setHorizontalGroup(
                reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, reportPanelLayout.createSequentialGroup()
                                .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(reportScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                                        .addComponent(reportActionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE))
                                .addContainerGap())
        );
        reportPanelLayout.setVerticalGroup(
                reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, reportPanelLayout.createSequentialGroup()
                                .addComponent(reportActionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(reportScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        mainTabbedPane.addTab("Reports", reportPanel);

        autoResponsePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Automated Responses"));

        autoAcknowledge.setText("Acknowledge orders on receipt");
        try {
            autoAcknowledge.setSelected(
                    FIXimulator.getApplication().getSettings()
                            .getBool("FIXimulatorAutoAcknowledge"));
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }
        autoAcknowledge.addActionListener(FIXimulatorFrame.this::autoAcknowledgeActionPerformed);

        autoPendingCancel.setText("Send Pending Cancel");
        try {
            autoPendingCancel.setSelected(
                    FIXimulator.getApplication().getSettings()
                            .getBool("FIXimulatorAutoPendingCancel"));
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }
        autoPendingCancel.addActionListener(FIXimulatorFrame.this::autoPendingCancelActionPerformed);

        autoPendingReplace.setText("Send Pending Replace");
        try {
            autoPendingReplace.setSelected(
                    FIXimulator.getApplication().getSettings()
                            .getBool("FIXimulatorAutoPendingReplace"));
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }
        autoPendingReplace.addActionListener(FIXimulatorFrame.this::autoPendingReplaceActionPerformed);

        autoCancel.setText("Accept order cancellations");
        try {
            autoCancel.setSelected(
                    FIXimulator.getApplication().getSettings()
                            .getBool("FIXimulatorAutoCancel"));
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }
        autoCancel.addActionListener(FIXimulatorFrame.this::autoCancelActionPerformed);

        autoReplace.setText("Accept order replacements");
        try {
            autoReplace.setSelected(
                    FIXimulator.getApplication().getSettings()
                            .getBool("FIXimulatorAutoReplace"));
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }
        autoReplace.addActionListener(FIXimulatorFrame.this::autoReplaceActionPerformed);

        javax.swing.GroupLayout autoResponsePanelLayout = new javax.swing.GroupLayout(autoResponsePanel);
        autoResponsePanel.setLayout(autoResponsePanelLayout);
        autoResponsePanelLayout.setHorizontalGroup(
                autoResponsePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(autoResponsePanelLayout.createSequentialGroup()
                                .addGroup(autoResponsePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(autoPendingCancel)
                                        .addComponent(autoCancel)
                                        .addComponent(cancelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(autoAcknowledge)
                                        .addComponent(replaceSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(autoPendingReplace)
                                        .addComponent(autoReplace))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        autoResponsePanelLayout.setVerticalGroup(
                autoResponsePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(autoResponsePanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(autoAcknowledge)
                                .addGap(6, 6, 6)
                                .addComponent(cancelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(autoPendingCancel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(autoCancel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(replaceSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(autoPendingReplace)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(autoReplace)
                                .addContainerGap(76, Short.MAX_VALUE))
        );

        saveSettingsButton.setText("Save Settings");
        saveSettingsButton.addActionListener(FIXimulatorFrame.this::saveSettingsButtonActionPerformed);

        appSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("FIXimulator Settings"));

        pricePrecisionLabel.setText("Price precision:");

        cachedObjectsLabel.setText("Number of cached objects:");

        cachedObjectsCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"50", "100", "200"}));

        pricePrecisionCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"}));
        try {
            Long settingValue = FIXimulator.getApplication().getSettings()
                    .getLong("FIXimulatorPricePrecision");
            if (settingValue >= 0 && settingValue < 10) {
                pricePrecisionCombo.setSelectedItem(settingValue.toString());
            } else {
                // default due to bad value
                pricePrecisionCombo.setSelectedItem("4");
                FIXimulator.getApplication().getSettings()
                        .setLong("FIXimulatorPricePrecision", 4);
            }
        } catch (Exception e) {
            LOG.error("Error: ", e);
            // default to to setting not existing
            pricePrecisionCombo.setSelectedItem("4");
            FIXimulator.getApplication().getSettings()
                    .setLong("FIXimulatorPricePrecision", 4);
        }
        pricePrecisionCombo.addActionListener(FIXimulatorFrame.this::pricePrecisionComboActionPerformed);

        sendOnBehalfOfCompID.setText("Send OnBehalfOfCompID (115)");
        try {
            sendOnBehalfOfCompID.setSelected(
                    FIXimulator.getApplication().getSettings()
                            .getBool("FIXimulatorSendOnBehalfOfCompID"));
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }
        sendOnBehalfOfCompID.addActionListener(this::sendOnBehalfOfCompIDActionPerformed);

        sendOnBehalfOfSubID.setText("Send OnBehalfOfSubID (116)");
        try {
            sendOnBehalfOfSubID.setSelected(
                    FIXimulator.getApplication().getSettings()
                            .getBool("FIXimulatorSendOnBehalfOfSubID"));
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }
        sendOnBehalfOfSubID.addActionListener(FIXimulatorFrame.this::sendOnBehalfOfSubIDActionPerformed);

        logToFileLabel.setText("<html>Changing the logging requires saving the settings and restarting the application...</htm;>");

        logToFile.setText("Log to file");
        try {
            logToFile.setSelected(
                    FIXimulator.getApplication().getSettings()
                            .getBool("FIXimulatorLogToFile"));
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }
        logToFile.addActionListener(FIXimulatorFrame.this::logToFileActionPerformed);

        logToDB.setText("Log to database");
        try {
            logToDB.setSelected(
                    FIXimulator.getApplication().getSettings()
                            .getBool("FIXimulatorLogToDB"));
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }
        logToDB.addActionListener(FIXimulatorFrame.this::logToDBActionPerformed);

        javax.swing.GroupLayout appSettingsPanelLayout = new javax.swing.GroupLayout(appSettingsPanel);
        appSettingsPanel.setLayout(appSettingsPanelLayout);
        appSettingsPanelLayout.setHorizontalGroup(
                appSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(appSettingsPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(appSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(logToFileLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(appSettingsPanelLayout.createSequentialGroup()
                                                .addComponent(logToFile)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(logToDB))
                                        .addComponent(oboCompIDSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                                        .addComponent(sendOnBehalfOfSubID)
                                        .addGroup(appSettingsPanelLayout.createSequentialGroup()
                                                .addGroup(appSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(cachedObjectsLabel)
                                                        .addComponent(pricePrecisionLabel))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(appSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(cachedObjectsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(pricePrecisionCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(oboCompIDSeparator, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                                        .addComponent(sendOnBehalfOfCompID))
                                .addContainerGap())
        );

        appSettingsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, cachedObjectsCombo, pricePrecisionCombo);

        appSettingsPanelLayout.setVerticalGroup(
                appSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(appSettingsPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(appSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(pricePrecisionLabel)
                                        .addComponent(pricePrecisionCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(appSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cachedObjectsLabel)
                                        .addComponent(cachedObjectsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(oboCompIDSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sendOnBehalfOfCompID)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sendOnBehalfOfSubID)
                                .addGap(12, 12, 12)
                                .addComponent(oboCompIDSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(logToFileLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(appSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(logToFile)
                                        .addComponent(logToDB))
                                .addContainerGap())
        );

        showSettingsButton.setText("Show Settings");
        showSettingsButton.addActionListener(FIXimulatorFrame.this::showSettingsButtonActionPerformed);

        javax.swing.GroupLayout settingsPanelLayout = new javax.swing.GroupLayout(settingsPanel);
        settingsPanel.setLayout(settingsPanelLayout);
        settingsPanelLayout.setHorizontalGroup(
                settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(settingsPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(settingsPanelLayout.createSequentialGroup()
                                                .addComponent(autoResponsePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(appSettingsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, settingsPanelLayout.createSequentialGroup()
                                                .addComponent(showSettingsButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(saveSettingsButton)))
                                .addContainerGap())
        );

        settingsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, saveSettingsButton, showSettingsButton);

        settingsPanelLayout.setVerticalGroup(
                settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, settingsPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(appSettingsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(autoResponsePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(saveSettingsButton)
                                        .addComponent(showSettingsButton))
                                .addContainerGap())
        );

        mainTabbedPane.addTab("Settings", settingsPanel);

        fileMenu.setText("File");

        mainMenuBar.add(fileMenu);

        instrumentMenu.setText("Instruments");

        loadInstrumentMenuItem.setText("Load Instruments...");
        loadInstrumentMenuItem.addActionListener(this::loadInstrumentMenuItemActionPerformed);
        instrumentMenu.add(loadInstrumentMenuItem);

        mainMenuBar.add(instrumentMenu);

        helpMenu.setText("Help");

        aboutMenuItem.setText("About...");
        aboutMenuItem.addActionListener(this::aboutMenuItemActionPerformed);
        helpMenu.add(aboutMenuItem);

        mainMenuBar.add(helpMenu);

        setJMenuBar(mainMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(messagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(mainTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(messageDetailPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(statusBarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(messageDetailPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(mainTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(messagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(statusBarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        bindingGroup.bind();

        pack();
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        aboutDialog.dispose();
    }

    private void symbolComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        FIXimulator.getApplication().setNewSymbol(
                symbolComboBox.getSelectedItem().toString());
    }

    private void securityIDComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        FIXimulator.getApplication().setNewSecurityID(
                securityIDComboBox.getSelectedItem().toString());
    }

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String symbol = symbolComboBox.getSelectedItem().toString();
        String securityID = securityIDComboBox.getSelectedItem().toString();
        int rate = rateSlider.getValue();
        if (rate == 0) rate = 1;
        Integer delay = 60000 / rate;
        FIXimulator.getApplication().startIOIsender(delay, symbol, securityID);
    }

    private void sliderChanged(javax.swing.event.ChangeEvent evt) {
        if (!rateSlider.getValueIsAdjusting()) {
            int rate = rateSlider.getValue();
            if (rate == 0) rate = 1;
            Integer newDelay = 60000 / rate;
            FIXimulator.getApplication().setNewDelay(newDelay);
        }
    }

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {
        FIXimulator.getApplication().stopIOIsender();
    }

    private void singleIOIButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_singleIOIButtonActionPerformed
        dialogIOI = new IOI();
        dialogIOI.setType("NEW");
        ioiDialog.setTitle("Add IOI...");
        ioiDialogID.setText(dialogIOI.id());
        ioiDialogShares.setValue(0);
        ioiDialogSymbol.setText("");
        ioiDialogSecurityID.setText("");
        ioiDialogPrice.setValue(0.0);
        ioiDialog.pack();
        ioiDialog.setVisible(true);
    }//GEN-LAST:event_singleIOIButtonActionPerformed

    private void startExecutorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startExecutorButtonActionPerformed
        int delay = 1;
        if (this.executorDelay.getSelectedItem().toString().equals("10 ms"))
            delay = 10;
        if (this.executorDelay.getSelectedItem().toString().equals("100 ms"))
            delay = 100;
        if (this.executorDelay.getSelectedItem().toString().equals("1 second"))
            delay = 1000;
        if (this.executorDelay.getSelectedItem().toString().equals("5 seconds"))
            delay = 5000;
        int partials = partialsSlider.getValue();
        if (partials == 0) partials = 1;
        FIXimulator.getApplication().startExecutor(delay, partials);
    }//GEN-LAST:event_startExecutorButtonActionPerformed

    private void stopExecutorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopExecutorButtonActionPerformed
        FIXimulator.getApplication().stopExecutor();
    }//GEN-LAST:event_stopExecutorButtonActionPerformed

    private void ioiDialogCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ioiDialogCancelActionPerformed
        ioiDialog.dispose();
    }//GEN-LAST:event_ioiDialogCancelActionPerformed

    private void ioiDialogOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ioiDialogOKActionPerformed
        // Set fields
        dialogIOI.setSide(ioiDialogSide.getSelectedItem().toString());
        dialogIOI.setQuantity(Integer.parseInt(ioiDialogShares.getText()));
        dialogIOI.setSymbol(ioiDialogSymbol.getText());
        dialogIOI.setSecurityID(ioiDialogSecurityID.getText());
        dialogIOI.setIDSource(ioiDialogIDSource.getSelectedItem().toString());
        dialogIOI.setPrice(Double.parseDouble(ioiDialogPrice.getText()));
        dialogIOI.setNatural(ioiDialogNatural.getSelectedItem().toString());
        FIXimulator.getApplication().sendIOI(dialogIOI);
        ioiDialog.dispose();
    }//GEN-LAST:event_ioiDialogOKActionPerformed

    private void cancelIOIButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelIOIButtonActionPerformed
        int row = ioiTable.getSelectedRow();
        // if there is a row selected
        if (row != -1) {
            row = ioiTable.convertRowIndexToModel(row);
            IOI ioi = ioiDataModel.get(row);
            IOI cancelIOI = ioi.clone();
            cancelIOI.setType("CANCEL");
            FIXimulator.getApplication().sendIOI(cancelIOI);
        }
    }//GEN-LAST:event_cancelIOIButtonActionPerformed

    private void replaceIOIButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_replaceIOIButtonActionPerformed
        int row = ioiTable.getSelectedRow();
        // if no rows are selected
        if (row != -1) {
            ioiDialog.setTitle("Replace IOI...");
            row = ioiTable.convertRowIndexToModel(row);
            IOI ioi = ioiDataModel.get(row);
            dialogIOI = ioi.clone();
            dialogIOI.setType("REPLACE");

            ioiDialogID.setText(dialogIOI.id());
            String side = dialogIOI.getSide();
            if (side.equals("BUY")) ioiDialogSide.setSelectedIndex(0);
            if (side.equals("SELL")) ioiDialogSide.setSelectedIndex(1);
            if (side.equals("UNDISCLOSED")) ioiDialogSide.setSelectedIndex(2);
            ioiDialogShares.setValue(dialogIOI.getQuantity());
            ioiDialogSymbol.setText(dialogIOI.getSymbol());
            ioiDialogSecurityID.setText(dialogIOI.getSecurityID());
            String idSource = dialogIOI.getIDSource();
            if (idSource.equals("CUSIP")) ioiDialogIDSource.setSelectedIndex(0);
            if (idSource.equals("SEDOL")) ioiDialogIDSource.setSelectedIndex(1);
            if (idSource.equals("RIC")) ioiDialogIDSource.setSelectedIndex(2);
            if (idSource.equals("TICKER")) ioiDialogIDSource.setSelectedIndex(3);
            if (idSource.equals("OTHER")) ioiDialogIDSource.setSelectedIndex(4);
            ioiDialogPrice.setValue(dialogIOI.getPrice());
            ioiDialogNatural.setSelectedIndex(0);
            if (dialogIOI.getNatural().equals("NO"))
                ioiDialogNatural.setSelectedIndex(1);
            ioiDialog.pack();
            ioiDialog.setVisible(true);
        }

    }//GEN-LAST:event_replaceIOIButtonActionPerformed

    private void acknowledgeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acknowledgeButtonActionPerformed
        int row = orderTable.getSelectedRow();
        // if no rows are selected
        if (row != -1) {
            row = orderTable.convertRowIndexToModel(row);
            Order order = orderTableModel.get(row);
            if (order.getStatus().equals("Received") ||
                    order.getStatus().equals("Pending New")) {
                FIXimulator.getApplication().acknowledge(order);
            } else {
                System.out.println(
                        "Order in status \"" + order.getStatus() + "\" " +
                                "cannot be acknowledged...");
            }
        }
    }//GEN-LAST:event_acknowledgeButtonActionPerformed

    private void partialsSliderChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_partialsSliderChanged
        if (!this.partialsSlider.getValueIsAdjusting()) {
            int partials = partialsSlider.getValue();
            if (partials == 0) partials = 1;
            System.out.println("The number of partials was changed to: " + partials);
            FIXimulator.getApplication().setNewExecutorPartials(partials);
        }
    }//GEN-LAST:event_partialsSliderChanged

    private void executionBustButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executionBustButtonActionPerformed
        int row = executionTable.getSelectedRow();
        // if there is a row selected
        if (row != -1) {
            Execution execution =
                    executionTableModel.get(row);

            if (execution.getExecType().equals("Fill") ||
                    execution.getExecType().equals("Partial fill")) {
                FIXimulator.getApplication().bust(execution);
            } else {
                System.out.println(
                        "\"" + execution.getExecType() + "\" " +
                                "executions cannot be busted...");
            }

        }
    }//GEN-LAST:event_executionBustButtonActionPerformed

    private void executorDelayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executorDelayActionPerformed
        int delay = 1;
        String value = executorDelay.getSelectedItem().toString();
        if (value.equals("10 ms")) delay = 10;
        if (value.equals("100 ms")) delay = 100;
        if (value.equals("1 second")) delay = 1000;
        if (value.equals("5 seconds")) delay = 5000;
        FIXimulator.getApplication().setNewExecutorDelay(delay);
    }//GEN-LAST:event_executorDelayActionPerformed

    private void autoReplaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoReplaceActionPerformed
        FIXimulator.getApplication().getSettings()
                .setBool("FIXimulatorAutoReplace",
                        autoReplace.isSelected());
    }//GEN-LAST:event_autoReplaceActionPerformed

    private void autoPendingCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoPendingCancelActionPerformed
        FIXimulator.getApplication().getSettings()
                .setBool("FIXimulatorAutoPendingCancel",
                        autoPendingCancel.isSelected());
    }//GEN-LAST:event_autoPendingCancelActionPerformed

    private void autoAcknowledgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoAcknowledgeActionPerformed
        FIXimulator.getApplication().getSettings()
                .setBool("FIXimulatorAutoAcknowledge",
                        autoAcknowledge.isSelected());
    }//GEN-LAST:event_autoAcknowledgeActionPerformed

    private void autoPendingReplaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoPendingReplaceActionPerformed
        FIXimulator.getApplication().getSettings()
                .setBool("FIXimulatorAutoPendingReplace",
                        autoPendingReplace.isSelected());
    }//GEN-LAST:event_autoPendingReplaceActionPerformed

    private void autoCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoCancelActionPerformed
        FIXimulator.getApplication().getSettings()
                .setBool("FIXimulatorAutoCancel",
                        autoCancel.isSelected());
    }//GEN-LAST:event_autoCancelActionPerformed

    private void saveSettingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveSettingsButtonActionPerformed
        FIXimulator.getApplication().saveSettings();
    }//GEN-LAST:event_saveSettingsButtonActionPerformed

    @SuppressWarnings("static-access")
    private void loadInstrumentMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadInstrumentMenuItemActionPerformed
        int result = instrumentFileChooser.showOpenDialog(this);
        if (result == instrumentFileChooser.APPROVE_OPTION) {
            File file = instrumentFileChooser.getSelectedFile();
            instrumentsApi.reloadInstrumentSet(file);
        } else {
            LOG.info("User cancelled loading file...");
        }
    }//GEN-LAST:event_loadInstrumentMenuItemActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        aboutDialog.pack();
        aboutDialog.setVisible(true);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void pricePrecisionComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pricePrecisionComboActionPerformed
        FIXimulator.getApplication().getSettings()
                .setLong("FIXimulatorPricePrecision",
                        Long.valueOf(pricePrecisionCombo.getSelectedItem().toString()));
    }//GEN-LAST:event_pricePrecisionComboActionPerformed

    private void sendOnBehalfOfCompIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendOnBehalfOfCompIDActionPerformed
        FIXimulator.getApplication().getSettings()
                .setBool("FIXimulatorSendOnBehalfOfCompID",
                        sendOnBehalfOfCompID.isSelected());
    }//GEN-LAST:event_sendOnBehalfOfCompIDActionPerformed

    private void sendOnBehalfOfSubIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendOnBehalfOfSubIDActionPerformed
        FIXimulator.getApplication().getSettings()
                .setBool("FIXimulatorSendOnBehalfOfSubID",
                        sendOnBehalfOfSubID.isSelected());
    }//GEN-LAST:event_sendOnBehalfOfSubIDActionPerformed

    private void showSettingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showSettingsButtonActionPerformed
        System.out.println(FIXimulator.getApplication().getSettings().toString());
    }//GEN-LAST:event_showSettingsButtonActionPerformed

    private void orderRejectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderRejectButtonActionPerformed
        int row = orderTable.getSelectedRow();
        // if no rows are selected
        if (row != -1) {
            row = orderTable.convertRowIndexToModel(row);
            Order order = orderTableModel.get(row);
            if (order.getStatus().equals("Received") ||
                    order.getStatus().equals("Pending New")) {
                FIXimulator.getApplication().reject(order);
            } else {
                System.out.println(
                        "Order in status \"" + order.getStatus() + "\" " +
                                "cannot be rejected...");
            }
        }
    }//GEN-LAST:event_orderRejectButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        int row = orderTable.getSelectedRow();
        // if no rows are selected
        if (row != -1) {
            row = orderTable.convertRowIndexToModel(row);
            Order order = orderTableModel.get(row);
            FIXimulator.getApplication().cancel(order);
        }
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void dfdButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dfdButtonActionPerformed
        int row = orderTable.getSelectedRow();
        // if no rows are selected
        if (row != -1) {
            row = orderTable.convertRowIndexToModel(row);
            Order order = orderTableModel.get(row);
            FIXimulator.getApplication().dfd(order);
        }
    }//GEN-LAST:event_dfdButtonActionPerformed

    private void cancelPendingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelPendingButtonActionPerformed
        int row = orderTable.getSelectedRow();
        // if no rows are selected
        if (row != -1) {
            row = orderTable.convertRowIndexToModel(row);
            Order order = orderTableModel.get(row);
            if (order.isReceivedCancel()) {
                FIXimulator.getApplication().pendingCancel(order);
            } else {
                System.out.println(
                        "Order is not in a valid status for pending cancel");
            }
        }
    }//GEN-LAST:event_cancelPendingButtonActionPerformed

    private void cancelAcceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelAcceptButtonActionPerformed
        int row = orderTable.getSelectedRow();
        // if no rows are selected
        if (row != -1) {
            row = orderTable.convertRowIndexToModel(row);
            Order order = orderTableModel.get(row);
            FIXimulator.getApplication().cancel(order);
        }
    }//GEN-LAST:event_cancelAcceptButtonActionPerformed

    private void replacePendingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_replacePendingButtonActionPerformed
        int row = orderTable.getSelectedRow();
        // if no rows are selected
        if (row != -1) {
            row = orderTable.convertRowIndexToModel(row);
            Order order = orderTableModel.get(row);
            if (order.isReceivedReplace()) {
                FIXimulator.getApplication().pendingReplace(order);
            } else {
                System.out.println(
                        "Order is not in a valid status for pending replace");
            }
        }
    }//GEN-LAST:event_replacePendingButtonActionPerformed

    private void replaceAcceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_replaceAcceptButtonActionPerformed
        int row = orderTable.getSelectedRow();
        // if no rows are selected
        if (row != -1) {
            row = orderTable.convertRowIndexToModel(row);
            Order order = orderTableModel.get(row);
            if (order.isReceivedReplace() ||
                    order.getStatus().equals("Pending Replace")) {
                FIXimulator.getApplication().replace(order);
            } else {
                System.out.println(
                        "Order is not in a valid status to replace");
            }
        }
    }//GEN-LAST:event_replaceAcceptButtonActionPerformed

    private void cancelRejectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelRejectButtonActionPerformed
        int row = orderTable.getSelectedRow();
        // if no rows are selected
        if (row != -1) {
            row = orderTable.convertRowIndexToModel(row);
            Order order = orderTableModel.get(row);
            if (order.isReceivedCancel() ||
                    order.getStatus().equals("Pending Cancel")) {
                FIXimulator.getApplication().rejectCancelReplace(order, true);
            } else {
                System.out.println(
                        "Order is not in a valid status to reject cancellation");
            }
        }
    }//GEN-LAST:event_cancelRejectButtonActionPerformed

    private void replaceRejectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_replaceRejectButtonActionPerformed
        int row = orderTable.getSelectedRow();
        // if no rows are selected
        if (row != -1) {
            row = orderTable.convertRowIndexToModel(row);
            Order order = orderTableModel.get(row);
            if (order.isReceivedReplace() ||
                    order.getStatus().equals("Pending Replace")) {
                FIXimulator.getApplication().rejectCancelReplace(order, false);
            } else {
                System.out.println(
                        "Order is not in a valid status to reject replace request");
            }
        }
    }//GEN-LAST:event_replaceRejectButtonActionPerformed

    private void executionDialogOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executionDialogOKActionPerformed
        dialogExecution.setLastShares(
                Integer.parseInt(executionDialogShares.getText()));
        dialogExecution.setLastPx(
                Double.parseDouble(executionDialogPrice.getText()));
        String refID = dialogExecution.getRefID();
        // New execution
        if (refID == null) {
            FIXimulator.getApplication().execute(dialogExecution);
            // Correction
        } else {
            FIXimulator.getApplication().correct(dialogExecution);
        }
        executionDialog.dispose();
    }//GEN-LAST:event_executionDialogOKActionPerformed

    private void executionDialogCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executionDialogCancelActionPerformed
        executionDialog.dispose();
    }//GEN-LAST:event_executionDialogCancelActionPerformed

    private void executeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executeButtonActionPerformed
        int row = orderTable.getSelectedRow();
        // if no rows are selected
        if (row != -1) {
            row = orderTable.convertRowIndexToModel(row);
            Order order = orderTableModel.get(row);
            dialogExecution = new Execution(order);
            executionDialogShares.setValue(0);
            executionDialogPrice.setValue(0.0);
            executionDialog.pack();
            executionDialog.setVisible(true);
        }

    }//GEN-LAST:event_executeButtonActionPerformed

    private void executionCorrectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executionCorrectButtonActionPerformed
        int row = executionTable.getSelectedRow();
        // if no rows are selected
        if (row != -1) {
            Execution execution =
                    executionTableModel.get(row);

            if (execution.getExecType().equals("Fill") ||
                    execution.getExecType().equals("Partial fill")) {
                dialogExecution = execution.clone();
                executionDialogShares.setValue(execution.getLastShares());
                executionDialogPrice.setValue(execution.getLastPx());
                executionDialog.pack();
                executionDialog.setVisible(true);
            } else {
                System.out.println(
                        "\"" + execution.getExecType() + "\" " +
                                "executions cannot be corrected...");
            }
        }
    }//GEN-LAST:event_executionCorrectButtonActionPerformed

    private void logToFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logToFileActionPerformed
        FIXimulator.getApplication().getSettings()
                .setBool("FIXimulatorLogToFile",
                        sendOnBehalfOfSubID.isSelected());
    }//GEN-LAST:event_logToFileActionPerformed

    private void logToDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logToDBActionPerformed
        FIXimulator.getApplication().getSettings()
                .setBool("FIXimulatorLogToDB",
                        sendOnBehalfOfSubID.isSelected());
    }//GEN-LAST:event_logToDBActionPerformed

    private void customQueryRunButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customQueryRunButtonActionPerformed
        QueryTableModel qtm = (QueryTableModel) reportTable.getModel();
        qtm.setQuery(queryText.getText().trim());
    }//GEN-LAST:event_customQueryRunButtonActionPerformed

    private void cannedQueryRunButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cannedQueryRunButtonActionPerformed
        QueryTableModel qtm = (QueryTableModel) reportTable.getModel();
        String can = cannedQueryCombo.getSelectedItem().toString();
        String symbol = querySymbolText.getText().trim().toLowerCase();
        String query = "";

        if (can.equals("Show last 10 IOIs..."))
            query = "select id,text from messages_log "
                    + "where text like '%35=6%' order by id desc limit 10;";
        if (can.equals("Show last 10 orders..."))
            query = "select id,text from messages_log "
                    + "where text like '%35=D%' order by id desc limit 10;";
        if (can.equals("Show last 10 executions..."))
            query = "select id,text from messages_log "
                    + "where text like '%35=8%' order by id desc limit 10;";
        if (can.equals("Show all IOIs where Symbol(55) is..."))
            query = "select id,text from messages_log "
                    + "where text like '%35=6%' and lower(text) like '%55="
                    + symbol + "%';";
        if (can.equals("Show all orders where Symbol(55) is..."))
            query = "select id,text from messages_log "
                    + "where text like '%35=D%' and lower(text) like '%55="
                    + symbol + "%';";
        if (can.equals("Show all executions where Symbol(55) is..."))
            query = "select id,text from messages_log "
                    + "where text like '%35=8%' and lower(text) like '%55="
                    + symbol + "%';";
        if (can.equals("Show all activity where Symbol(55) is..."))
            query = "select id,text from messages_log "
                    + "where lower(text) like '%55=" + symbol + "%';";

        qtm.setQuery(query);

        if (reportTable.getColumnCount() > 1) {
            reportTable.getColumnModel().getColumn(0).setPreferredWidth(50);
            reportTable.getColumnModel().getColumn(1).setPreferredWidth(1000);
        }
    }//GEN-LAST:event_cannedQueryRunButtonActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            OrderRepositoryWithCallback orderRepository = new InMemoryOrderRepository();
            RepositoryWithCallback<Execution> executionRepository = new InMemoryRepository<>();
            RepositoryWithCallback<IOI> ioiRepository = new InMemoryRepository<>();

            URL instrumentsXml = FIXimulatorFrame.class.getClassLoader().getResource("config/instruments.xml");

            assert instrumentsXml != null;
            InstrumentsApi instrumentsApi = new InstrumentRepository(new File(instrumentsXml.getPath()));

            try {
                fiximulator = new FIXimulator(orderRepository, executionRepository, ioiRepository, instrumentsApi);
            } catch (FileNotFoundException e) {
                LOG.error("Error: ", e);
                return;
            }
            fiximulator.start();
            new FIXimulatorFrame(orderRepository, executionRepository, ioiRepository, instrumentsApi).setVisible(true);
        });
    }

    private JDialog aboutDialog;
    private JCheckBox autoAcknowledge;
    private JCheckBox autoCancel;
    private JCheckBox autoPendingCancel;
    private JCheckBox autoPendingReplace;
    private JCheckBox autoReplace;
    private JComboBox cannedQueryCombo;
    private JDialog executionDialog;
    private JFormattedTextField executionDialogPrice;
    private JFormattedTextField executionDialogShares;
    private JTable executionTable;
    private JComboBox executorDelay;
    private JFileChooser instrumentFileChooser;
    private JDialog ioiDialog;
    private JLabel ioiDialogID;
    private JComboBox ioiDialogIDSource;
    private JComboBox ioiDialogNatural;
    private JFormattedTextField ioiDialogPrice;
    private JTextField ioiDialogSecurityID;
    private JFormattedTextField ioiDialogShares;
    private JComboBox ioiDialogSide;
    private JTextField ioiDialogSymbol;
    private JTable ioiTable;
    private JTable orderTable;
    private JSlider partialsSlider;
    private JComboBox pricePrecisionCombo;
    private JTextField querySymbolText;
    private JTextField queryText;
    private JSlider rateSlider;
    private JTable reportTable;
    private JComboBox securityIDComboBox;
    private JCheckBox sendOnBehalfOfCompID;
    private JCheckBox sendOnBehalfOfSubID;
    private JComboBox symbolComboBox;
}

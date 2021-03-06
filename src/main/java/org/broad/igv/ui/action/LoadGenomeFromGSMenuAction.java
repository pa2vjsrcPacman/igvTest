/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2007-2015 Broad Institute
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.broad.igv.ui.action;

import org.apache.log4j.Logger;
import org.broad.igv.feature.genome.GenomeManager;
import org.broad.igv.gs.GSFileBrowser;
import org.broad.igv.ui.IGV;
import org.broad.igv.ui.util.MessageUtils;

import java.awt.event.ActionEvent;

/**
 * @author jrobinso
 *         Date: 4/24/13
 *         Time: 4:26 PM
 */
public class LoadGenomeFromGSMenuAction extends MenuAction {

    static Logger log = Logger.getLogger(LoadGenomeFromGSMenuAction.class);
    IGV igv;

    public LoadGenomeFromGSMenuAction(String label, int mnemonic, IGV igv) {
        super(label, null, mnemonic);
        this.igv = igv;
        setToolTipText("Load genome from GenomeSpace");
    }

    @Override
    public void actionPerformed(ActionEvent e) {


        try {
            GSFileBrowser dlg = new GSFileBrowser(IGV.getMainFrame());
            dlg.setVisible(true);

            String url = dlg.getFileURL();
            if (url != null) {
                GenomeManager.getInstance().loadGenome(url, null);
            }
        } catch (Exception e1) {
            log.error("Error fetching directory listing on GenomeSpace server.", e1);
            MessageUtils.showMessage("Error fetching directory listing on GenomeSpace server: " + e1.getMessage());
        }

    }

}

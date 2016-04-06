/*
 * Title: CloudSim Toolkit Description: CloudSim (Cloud Simulation) Toolkit for Modeling and
 * Simulation of Clouds Licence: GPL - http://www.gnu.org/copyleft/gpl.html
 * 
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */
package org.cloudbus.cloudsim;

import org.cloudbus.cloudsim.schedulers.CloudletSchedulerAbstract;
import org.cloudbus.cloudsim.schedulers.CloudletSchedulerDynamicWorkload;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;
import org.cloudbus.cloudsim.listeners.EventListener;
import org.cloudbus.cloudsim.schedulers.CloudletScheduler;
import org.cloudbus.cloudsim.schedulers.CloudletSchedulerTimeShared;

import org.junit.Before;
import org.junit.Test;
import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Anton Beloglazov
 * @since CloudSim Toolkit 2.0
 */
public class VmSimpleTest {

    private static final int ID = 1;
    private static final int USER_ID = 1;
    private static final double MIPS = 1000;
    private static final int PES_NUMBER = 2;
    private static final int RAM = 1024;
    private static final int BW = 10000;
    private static final long SIZE = 1000;
    private static final String VMM = "Xen";
    private CloudletSchedulerDynamicWorkload vmScheduler;
    private VmSimple vm;

    @Before
    public void setUp() throws Exception {
        vmScheduler = new CloudletSchedulerDynamicWorkload(MIPS, PES_NUMBER);
        vm = createVmWithDefaultConfiguration(vmScheduler);
    }

    /**
     * Creates a VM with the 1 PE and half mips capacity defined in
     * {@link #MIPS}.
     *
     * @param vmId the id of the VM
     * @return
     */
    public static VmSimple createVmWithOnePeAndHalfMips(final int vmId) {
        return VmSimpleTest.createVm(vmId, MIPS / 2, 1, RAM, BW, SIZE, null);
    }

    /**
     * Creates a VM with 1 PE and the total mips capacity defined in
     * {@link #MIPS}.
     *
     * @param vmId the id of the VM
     * @return
     */
    public static VmSimple createVmWithOnePeAndTotalMips(final int vmId) {
        return VmSimpleTest.createVm(vmId, MIPS, 1, RAM, BW, SIZE, null);
    }

    /**
     * Creates a VM with the given mips and numberOfPes and default
     * configuration for RAM, BW and Storage.
     *
     * @param vmId
     * @param mips
     * @param numberOfPes
     * @return
     */
    public static VmSimple createVmWithSpecificMipsAndNumberOfPEs(final int vmId,
            final double mips, final int numberOfPes) {
        return new VmSimple(vmId, 0, mips, numberOfPes, RAM, BW, SIZE, "", null);
    }

    /**
     * Creates a VM with the given numberOfPes and default configuration for
     * MIPS, RAM, BW and Storage.
     *
     * @param vmId
     * @param numberOfPes
     * @return
     */
    public static VmSimple createVmWithSpecificNumberOfPEs(final int vmId, final int numberOfPes) {
        return new VmSimple(vmId, 0, MIPS, numberOfPes, RAM, BW, SIZE, "", null);
    }

    /**
     * Creates a VM with the given configuration
     *
     * @param vmId
     * @param mips
     * @param numberOfPes
     * @param ram
     * @param bw
     * @param storage
     * @param scheduler the cloudlet scheduler
     * @return
     */
    public static VmSimple createVm(final int vmId,
            final double mips, final int numberOfPes,
            final int ram, final long bw, final long storage,
            final CloudletSchedulerAbstract scheduler) {
        return new VmSimple(vmId, 0, mips, numberOfPes, ram, bw, storage, "", scheduler);
    }

    /**
     * Creates a VM with the given numberOfPes for a given user and default
     * configuration for MIPS, RAM, BW and Storage.
     *
     * @param vmId
     * @param userId
     * @param numberOfPes
     * @return
     */
    public static VmSimple createVmWithSpecificNumberOfPEsForSpecificUser(
            final int vmId, final int userId, final int numberOfPes) {
        return new VmSimple(vmId, userId, MIPS, numberOfPes, RAM, BW, SIZE, "", null);
    }

    @Test
    public void testGetMips() {
        assertEquals(MIPS, vm.getMips(), 0);
    }

    @Test
    public void testSetMips() {
        vm.setMips(MIPS / 2);
        assertEquals(MIPS / 2, vm.getMips(), 0);
    }

    @Test
    public void testSetRam() {
        assertTrue(vm.setRam(RAM / 2));
        assertEquals(RAM / 2, vm.getRam(), 0);
    }

    @Test
    public void testSetBw() {
        assertTrue(vm.setBw(BW / 2));
        assertEquals(BW / 2, vm.getBw(), 0);
    }

    @Test
    public void testSetOnHostAllocationListener() {
        vm.setOnHostAllocationListener(null);
        assertEquals(EventListener.NULL, vm.getOnHostAllocationListener());
        EventListener<Vm, Host> listener = (t, v, h) -> {};
        vm.setOnHostAllocationListener(listener);
        assertEquals(listener, vm.getOnHostAllocationListener());
    }

    @Test
    public void testSetOnHostDeallocationListener() {
        vm.setOnHostDeallocationListener(null);
        assertEquals(EventListener.NULL, vm.getOnHostDeallocationListener());
        EventListener<Vm, Host> listener = (t, v, h) -> {};
        vm.setOnHostDeallocationListener(listener);
        assertEquals(listener, vm.getOnHostDeallocationListener());
    }

    @Test
    public void testSetOnVmCreationFailureListener() {
        vm.setOnVmCreationFailureListener(null);
        assertEquals(EventListener.NULL, vm.getOnVmCreationFailureListener());
        EventListener<Vm, Datacenter> listener = (t, v, d) -> {};
        vm.setOnVmCreationFailureListener(listener);
        assertEquals(listener, vm.getOnVmCreationFailureListener());
    }

    @Test
    public void testSetOnUpdateVmProcessingListener() {
        vm.setOnUpdateVmProcessingListener(null);
        assertEquals(EventListener.NULL, vm.getOnUpdateVmProcessingListener());
        EventListener<Vm, Host> listener = (t, v, h) -> {};
        vm.setOnUpdateVmProcessingListener(listener);
        assertEquals(listener, vm.getOnUpdateVmProcessingListener());
    }

    @Test
    public void testGetNumberOfPes() {
        assertEquals(PES_NUMBER, vm.getNumberOfPes());
    }

    @Test
    public void testGetRam() {
        assertEquals(RAM, vm.getRam());
    }

    @Test
    public void testGetBw() {
        assertEquals(BW, vm.getBw());
    }

    @Test
    public void testGetSize() {
        assertEquals(SIZE, vm.getSize());
    }

    @Test
    public void testGetVmm() {
        assertEquals(VMM, vm.getVmm());
    }

    @Test
    public void testGetHost() {
        assertEquals(null, vm.getHost());
        HostSimple host = HostSimpleTest.createHostSimple(0, 1);
        vm.setHost(host);
        assertEquals(host, vm.getHost());
    }

    @Test
    public void testIsInMigration() {
        assertFalse(vm.isInMigration());
        vm.setInMigration(true);
        assertTrue(vm.isInMigration());
    }

    @Test
    public void testGetTotalUtilization() {
        assertEquals(0, vm.getTotalUtilizationOfCpu(0), 0);
    }

    @Test
    public void testGetTotalUtilizationMips() {
        assertEquals(0, vm.getTotalUtilizationOfCpuMips(0), 0);
    }

    @Test
    public void testGetUid() {
        assertEquals(USER_ID + "-" + ID, vm.getUid());
    }

    @Test
    public void testUpdateVmProcessing() {
        assertEquals(0, vm.updateVmProcessing(0, null), 0);
        ArrayList<Double> mipsShare1 = new ArrayList<>();
        mipsShare1.add(1.0);
        ArrayList<Double> mipsShare2 = new ArrayList<>();
        mipsShare2.add(1.0);
        assertEquals(vmScheduler.updateVmProcessing(0, mipsShare1), vm.updateVmProcessing(0, mipsShare2), 0);
    }

    @Test
    public void testGetCurrentAllocatedSize() {
        assertEquals(0, vm.getCurrentAllocatedSize());
        vm.setCurrentAllocatedSize(SIZE);
        assertEquals(SIZE, vm.getCurrentAllocatedSize());
    }

    @Test
    public void testGetCurrentAllocatedRam() {
        assertEquals(0, vm.getCurrentAllocatedRam());
        vm.setCurrentAllocatedRam(RAM);
        assertEquals(RAM, vm.getCurrentAllocatedRam());
    }

    @Test
    public void testGetCurrentAllocatedBw() {
        assertEquals(0, vm.getCurrentAllocatedBw());
        vm.setCurrentAllocatedBw(BW);
        assertEquals(BW, vm.getCurrentAllocatedBw());
    }

    @Test
    public void testGetCurrentAllocatedMips() {
        // ArrayList<Integer> currentAllocatedMips = new ArrayList<>();
        // assertEquals(currentAllocatedMips, vm.getCurrentAllocatedMips());
        assertNull(vm.getCurrentAllocatedMips());
    }

    @Test
    public void testIsBeingInstantiated() {
        assertTrue(vm.isBeingInstantiated());
        vm.setBeingInstantiated(false);
        assertFalse(vm.isBeingInstantiated());
    }

    @Test
    public void testGetCurrentRequestedMips() {
        CloudletScheduler  cloudletScheduler = createMock(CloudletScheduler.class);
        VmSimple vm = createVmWithDefaultConfiguration(cloudletScheduler);
        vm.setBeingInstantiated(false);

        List<Double> expectedCurrentMips = new ArrayList<>();
        expectedCurrentMips.add(MIPS / 2);
        expectedCurrentMips.add(MIPS / 2);

        expect(cloudletScheduler.getCurrentRequestedMips()).andReturn(expectedCurrentMips);

        replay(cloudletScheduler);

        assertEquals(expectedCurrentMips, vm.getCurrentRequestedMips());

        verify(cloudletScheduler);
    }

    @Test
    public void testGetCurrentRequestedMipsTimeSharedScheduler() {
        CloudletScheduler cloudletScheduler = new CloudletSchedulerTimeShared();
        VmSimple vm = createVmWithDefaultConfiguration(cloudletScheduler);
        vm.setBeingInstantiated(false);

        assertTrue(vm.getCurrentRequestedMips().isEmpty());
    }

    /**
     * Creates a VM with the default configuration defined in the Test Class'
     * constants.
     *
     * @param cloudletScheduler
     * @return
     */
    public static VmSimple createVmWithDefaultConfiguration(CloudletScheduler cloudletScheduler) {
        return new VmSimple(ID, USER_ID, MIPS, PES_NUMBER, RAM, BW, SIZE, VMM, cloudletScheduler);
    }

    @Test
    public void testGetCurrentRequestedTotalMips() {
        CloudletSchedulerAbstract cloudletScheduler = createMock(CloudletSchedulerAbstract.class);
        VmSimple vm = createVmWithDefaultConfiguration(cloudletScheduler);

        ArrayList<Double> currentMips = new ArrayList<>();
        currentMips.add(MIPS);
        currentMips.add(MIPS);

        expect(cloudletScheduler.getCurrentRequestedMips()).andReturn(currentMips);

        replay(cloudletScheduler);

        assertEquals(MIPS * 2, vm.getCurrentRequestedTotalMips(), 0);

        verify(cloudletScheduler);
    }

}